/* This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package info.somethingodd.bukkit.OddItem;

import info.somethingodd.bukkit.OddItem.bktree.BKTree;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public final class OddItemConfiguration {
    private OddItemBase oddItemBase = null;
    private int version;

    public OddItemConfiguration(OddItemBase OddItemBase) {
        this.oddItemBase = OddItemBase;
    }

    public void configure(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists())
            if (!writeConfig(file)) throw new Exception("Could not create configuration file!");
        OddItem.itemMap = new ConcurrentHashMap<String, ItemStack>();
        OddItem.items = new ConcurrentSkipListMap<String, NavigableSet<String>>();
        OddItem.groups = new ConcurrentSkipListMap<String, OddItemGroup>();
        Configuration configuration = new Configuration(file);
        configuration.load();
        version = configuration.getInt("listversion", 0);
        String comparator = configuration.getString("comparator");
        if (comparator != null) {
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
        } else {
            OddItem.bktree = new BKTree<String>("l");
            oddItemBase.log.info(oddItemBase.logPrefix + "Using Levenshtein for suggestions.");
        }
        ConfigurationNode itemsNode = configuration.getNode("items");
        for (String i : itemsNode.getKeys()) {
            Integer id;
            Short d = 0;
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
                OddItem.items.put(i, new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER));
            List<String> itemAliases = new ArrayList<String>();
            itemAliases.addAll(configuration.getStringList("items." + i, new ArrayList<String>()));
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
        ConfigurationNode groupsNode = configuration.getNode("groups");
        if (OddItem.groups != null) {
            for (String g : groupsNode.getKeys()) {
                List<String> i = new ArrayList<String>();
                if (groupsNode.getKeys(g) == null) {
                    i.addAll(groupsNode.getStringList(g, new ArrayList<String>()));
                    groupsNode.removeProperty(g);
                    groupsNode.setProperty(g + ".items", i);
                    groupsNode.setProperty(g + ".data", "null");
                } else {
                    i.addAll(groupsNode.getStringList(g + ".items", new ArrayList<String>()));
                }
                List<ItemStack> itemStackList = new ArrayList<ItemStack>();
                for (String is : i) {
                    ItemStack itemStack;
                    Integer q = null;
                    try {
                        if (is.contains(",")) {
                            q = Integer.valueOf(is.substring(is.indexOf(",") + 1));
                            is = is.substring(0, is.indexOf(","));
                            itemStack = OddItem.getItemStack(is, q);
                        } else {
                            itemStack = OddItem.getItemStack(is);
                        }
                        oddItemBase.log.info(oddItemBase.logPrefix + "Adding " + is + (q != null ? " x" + q : "") + " to group \"" + g + "\"");
                        if (itemStack != null) itemStackList.add(itemStack);
                    } catch (IllegalArgumentException e) {
                        oddItemBase.log.warning(oddItemBase.logPrefix + "Invalid item \"" + is + "\" in group \"" + g + "\"");
                        OddItem.groups.remove(g);
                    } catch (NullPointerException e) {
                        oddItemBase.log.warning(oddItemBase.logPrefix + "NPE adding ItemStack \"" + is + "\" to group " + g);
                    }
                    OddItem.groups.put(g, new OddItemGroup(itemStackList, groupsNode.getNode(g + ".data")));
                }
                if (OddItem.groups.get(g) != null) oddItemBase.log.info(oddItemBase.logPrefix + "Group " + g + " added.");
            }
        }
        configuration.save();
    }

    private boolean writeConfig(File file) {
        FileWriter fw;
        if (!file.getParentFile().exists()) file.getParentFile().mkdir();
        try {
            fw = new FileWriter(file);
        } catch (IOException e) {
            oddItemBase.log.severe(oddItemBase.logPrefix + "Couldn't write config file: " + e.getMessage());
            Bukkit.getServer().getPluginManager().disablePlugin(Bukkit.getServer().getPluginManager().getPlugin("OddItem"));
            return false;
        }
        BufferedReader i = new BufferedReader(new InputStreamReader(new BufferedInputStream(oddItemBase.getClass().getResourceAsStream("/OddItem.yml"))));
        BufferedWriter o = new BufferedWriter(fw);
        try {
            String line = i.readLine();
            while (line != null) {
                o.write(line + System.getProperty("line.separator"));
                line = i.readLine();
            }
            oddItemBase.log.info(oddItemBase.logPrefix + "Wrote default config");
        } catch (IOException e) {
            oddItemBase.log.severe(oddItemBase.logPrefix + "Error writing config: " + e.getMessage());
        } finally {
            try {
                o.close();
                i.close();
            } catch (IOException e) {
                oddItemBase.log.severe(oddItemBase.logPrefix + "Error saving config: " + e.getMessage());
                Bukkit.getServer().getPluginManager().disablePlugin(Bukkit.getServer().getPluginManager().getPlugin("OddItem"));
            }
        }
        return true;
    }

    private int getVersion() {
        return version;
    }
}
