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
import info.somethingodd.bukkit.OddItem.configuration.OddItemAliases;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemConfiguration {
    private final OddItemBase oddItemBase;

    public OddItemConfiguration(OddItemBase oddItemBase) {
        this.oddItemBase = oddItemBase;
    }

    public void configure() {
        File configurationFile = new File(oddItemBase.getDataFolder() + File.separator + "OddItem.yml");
        YamlConfiguration defaultConfiguration = new YamlConfiguration();
        try {
            defaultConfiguration.load(oddItemBase.getResource("OddItem.yml"));
        } catch (Exception e) {
            oddItemBase.log.warning(oddItemBase.logPrefix + "Error loading default configuration: " + e.getMessage());
            e.printStackTrace();
        }
        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(configurationFile);
            configuration.setDefaults(defaultConfiguration);
            configuration.save(configurationFile);
        } catch (Exception e) {
            oddItemBase.log.warning(oddItemBase.logPrefix + "Error loading configuration: " + e.getMessage());
            e.printStackTrace();
        }

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

        OddItem.items = Collections.synchronizedMap(new HashMap<String, ItemStack>());
        OddItem.aliases = Collections.synchronizedMap(new HashMap<ItemStack, Set<String>>());
        OddItem.oddItemAliases = (OddItemAliases) configuration.get("items");
    }

    public static String getComparator() {
        String comparator = "l";
        return new String(comparator);
    }
}
