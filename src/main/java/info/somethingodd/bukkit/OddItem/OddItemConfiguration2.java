/* This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, "either" version 3 of the License, "or"
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, "see" <http://www.gnu.org/licenses/>.
 */
package info.somethingodd.bukkit.OddItem;

import info.somethingodd.bukkit.OddItem.bktree.BKTree;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemConfiguration2 {
    private OddItemBase oddItemBase;
    private File configurationFile;
    private YamlConfiguration defaultConfiguration;
    private YamlConfiguration configuration;

    public OddItemConfiguration2(OddItemBase oddItemBase) {
        this.oddItemBase = oddItemBase;
    }

    public void configure() {
        configurationFile = new File(oddItemBase.getDataFolder() + File.separator + "OddItem.yml");
        if (!configurationFile.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(oddItemBase.getResource("OddItem.yml")));
            StringBuilder stringBuilder = new StringBuilder();
            try {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append('\n');
                }
            } catch (IOException e) {
                oddItemBase.log.warning(oddItemBase.logPrefix + "Error writing configuration: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    oddItemBase.log.severe(oddItemBase.logPrefix + "Couldn't close a file!!!" + e.getMessage());
                    e.printStackTrace();
                }
            }
            BufferedWriter bufferedWriter;
            try {
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configurationFile)));
                bufferedWriter.write(stringBuilder.toString());
                bufferedWriter.close();
            } catch (IOException e) {
                oddItemBase.log.warning(oddItemBase.logPrefix + "Couldn't write configuration..." + e.getMessage());
                e.printStackTrace();
            }
        }


        defaultConfiguration = new YamlConfiguration();
        try {
            defaultConfiguration.load(oddItemBase.getResource("OddItem.yml"));
        } catch (Exception e) {
            oddItemBase.log.warning(oddItemBase.logPrefix + "Error loading default configuration: " + e.getMessage());
            e.printStackTrace();
        }
        configuration = new YamlConfiguration();
        try {
            configuration.load(configurationFile);
            configuration.setDefaults(defaultConfiguration);
        } catch (Exception e) {
            oddItemBase.log.warning(oddItemBase.logPrefix + "Error loading configuration: " + e.getMessage());
            e.printStackTrace();
        }

        OddItem.itemMap = Collections.synchronizedMap(new HashMap<String, ItemStack>());
        OddItem.items = Collections.synchronizedMap(new HashMap<String, Set<String>>());
        OddItem.groups = Collections.synchronizedMap(new HashMap<String, OddItemGroup>());

        String comparator = configuration.getString("comparator");
        if (comparator.equalsIgnoreCase("c") || comparator.equalsIgnoreCase("caverphone")) {
            OddItem.bktree = new BKTree<String>("c");
            oddItemBase.log.info(oddItemBase.logPrefix + "Using Caverphone for suggestions.");
        } else if (comparator.equalsIgnoreCase("k") || comparator.equalsIgnoreCase("cologne")) {
            OddItem.bktree = new BKTree<String>("k");
            oddItemBase.log.info(oddItemBase.logPrefix + "Using ColognePhonetic for suggestions.");
        } else if (comparator.equalsIgnoreCase("m") || comparator.equalsIgnoreCase("metaphone")) {
            OddItem.bktree = new BKTree<String>("m");
            oddItemBase.log.info(oddItemBase.logPrefix + "Using Metaphone for suggestions.");
        } else if (comparator.equalsIgnoreCase("s") || comparator.equalsIgnoreCase("soundex")) {
            OddItem.bktree = new BKTree<String>("s");
            oddItemBase.log.info(oddItemBase.logPrefix + "Using SoundEx for suggestions.");
        } else if (comparator.equalsIgnoreCase("r") || comparator.equalsIgnoreCase("refinedsoundex")) {
            OddItem.bktree = new BKTree<String>("r");
            oddItemBase.log.info(oddItemBase.logPrefix + "Using RefinedSoundEx for suggestions.");
        } else {
            OddItem.bktree = new BKTree<String>("l");
            oddItemBase.log.info(oddItemBase.logPrefix + "Using Levenshtein for suggestions.");
        }

        ConfigurationSection itemsSection = configuration.getConfigurationSection("items");
        for (String i : itemsSection.getKeys(false)) {
            int id;
            short d = 0;
            Material m;
            if (i.contains(";")) {
                try {
                    d = Short.parseShort(i.substring(i.indexOf(";") + 1));
                    id = Integer.parseInt(i.substring(0, i.indexOf(";")));
                    m = Material.getMaterial(id);
                } catch (NumberFormatException e) {
                    m = Material.getMaterial(i.substring(0, i.indexOf(";")));
                    id = m.getId();
                }
            } else {
                try {
                    id = Integer.decode(i);
                    m = Material.getMaterial(id);
                } catch (NumberFormatException e) {
                    m = Material.getMaterial(i);
                    id = m.getId();
                }
            }
            if (OddItem.items.get(i) == null)
                OddItem.items.put(i, Collections.synchronizedSet(new TreeSet<String>(String.CASE_INSENSITIVE_ORDER)));
            List<String> itemAliases = new ArrayList<String>();
            itemAliases.addAll(itemsSection.getStringList(i));
            itemAliases.add(id + ";" + d);
            // Add all aliases
            OddItem.items.get(i).addAll(itemAliases);
            if (m != null) {
                for (String itemAlias : itemAliases) {
                    OddItem.itemMap.put(itemAlias, new ItemStack(m, 1, d));
                    OddItem.bktree.add(itemAlias);
                }
            } else {
                oddItemBase.log.warning(oddItemBase.logPrefix + "Invalid format: " + i);
            }
        }

    }
}
