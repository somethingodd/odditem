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

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import info.somethingodd.bukkit.OddItem.bktree.BKTree;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItem extends JavaPlugin {
    protected ConcurrentMap<String, ItemStack> itemMap;
	protected ConcurrentNavigableMap<String, SortedSet<String>> items;
    protected ConcurrentHashMap<String, OddItemGroup> groups;
	protected Logger log = null;
	protected String logPrefix;
    protected Configuration configuration = null;
    private BKTree<String> bktree = null;
	private PluginDescriptionFile info;
    private String permission = null;
    private PermissionHandler ph = null;
    private final String configurationFile = "plugins" + File.separator + "OddItem.yml";

    /**
     * Compares two ItemStack material and durability, ignoring quantity
     * @param a ItemStack to compare
     * @param b ItemStack to compare
     * @return ItemStack are equal
     */
    public static Boolean compare(ItemStack a, ItemStack b) {
        return compare(a, b, false, true, true);
    }

    /**
     * Compares two ItemStack material, durability, and quantity
     * @param a ItemStack to compare
     * @param b ItemStack to compare
     * @param quantity whether to compare quantity
     * @return ItemStack are equal
     */
    public static Boolean compare(ItemStack a, ItemStack b, Boolean quantity) {
        return compare(a, b, quantity, true, true);
    }

    /**
     * Compares two ItemStack
     * @param a ItemStack to compare
     * @param b ItemStack to compare
     * @param quantity whether to compare quantity
     * @param material whether to compare material
     * @param durability whether to compare durability
     * @return ItemStack are equal
     */
    public static Boolean compare(ItemStack a, ItemStack b, Boolean quantity, Boolean material, Boolean durability) {
        Boolean ret = true;
        if (quantity) ret &= (a.getAmount() == b.getAmount());
        if (ret && material) ret &= (a.getTypeId() == b.getTypeId());
        if (ret && durability) ret &= (a.getDurability() == b.getDurability());
        return ret;
    }

    protected void configure() {
        File configurationFile = new File(this.configurationFile);
        if (!configurationFile.exists())
            writeConfig();
        configuration = new Configuration(configurationFile);
        configuration.load();
        permission = configuration.getString("permission", "yeti");
        String comparator = configuration.getString("comparator");
        if (comparator != null) {
            if (comparator.equalsIgnoreCase("c") || comparator.equalsIgnoreCase("caverphone")) {
                bktree = new BKTree<String>("c");
                log.info(logPrefix + "Using Caverphone for suggestions.");
            } else if (comparator.equalsIgnoreCase("k") || comparator.equalsIgnoreCase("cologne")) {
                bktree = new BKTree<String>("k");
                log.info(logPrefix + "Using ColognePhonetic for suggestions.");
            } else if (comparator.equalsIgnoreCase("m") || comparator.equalsIgnoreCase("metaphone")) {
                bktree = new BKTree<String>("m");
                log.info(logPrefix + "Using Metaphone for suggestions.");
            } else if (comparator.equalsIgnoreCase("s") || comparator.equalsIgnoreCase("soundex")) {
                bktree = new BKTree<String>("s");
                log.info(logPrefix + "Using SoundEx for suggestions.");
            } else if (comparator.equalsIgnoreCase("r") || comparator.equalsIgnoreCase("refinedsoundex")) {
                bktree = new BKTree<String>("r");
                log.info(logPrefix + "Using RefinedSoundEx for suggestions.");
            } else {
                bktree = new BKTree<String>("l");
                log.info(logPrefix + "Using Levenshtein for suggestions.");
            }
        } else {
            bktree = new BKTree<String>("l");
            log.info(logPrefix + "Using Levenshtein for suggestions.");
        }
        ConfigurationNode items = configuration.getNode("items");
        for(String i : items.getKeys()) {
            if (this.items.get(i) == null)
                this.items.put(i, new ConcurrentSkipListSet<String>(String.CASE_INSENSITIVE_ORDER));
            ArrayList<String> j = new ArrayList<String>();
            j.addAll(configuration.getStringList("items." + i, new ArrayList<String>()));
            j.add(i);
            // Add all aliases
            this.items.get(i).addAll(j);
            Integer id = 0;
            Short d = 0;
            Material m = null;
            if (i.contains(";")) {
                try {
                    d = Short.parseShort(i.substring(i.indexOf(";") + 1));
                    id = Integer.parseInt(i.substring(0, i.indexOf(";")));
                    m = Material.getMaterial(id);
                } catch (NumberFormatException e) {
                    m = Material.getMaterial(i.substring(0, i.indexOf(";")));
                }
            } else {
                try {
                    id = Integer.decode(i);
                    m = Material.getMaterial(id);
                } catch (NumberFormatException e) {
                    m = Material.getMaterial(i);
                }
            }
            if (m == null) {
                log.warning(logPrefix + "Invalid format: " + i);
                continue;
            }
            for (String item : j) {
                itemMap.put(item, new ItemStack(m, 1, d));
                bktree.add(item);
            }
        }
        ConfigurationNode groups = configuration.getNode("groups");
        if (groups != null) {
            for (String g : groups.getKeys()) {
                List<String> i = new ArrayList<String>();
                if (this.groups.get(g) == null)
                    this.groups.put(g, new OddItemGroup());
                if (groups.getKeys(g) == null) {
                    i.addAll(groups.getStringList(g, new ArrayList<String>()));
                    groups.setProperty(g+".items", i);
                    groups.setProperty(g+".type", "unknown");
                } else {
                    i.addAll(groups.getStringList(g+".items", new ArrayList<String>()));
                }
                for (String is : i) {
                    try {
                        ItemStack itemStack = getItemStack(is);
                        this.groups.get(g).add(itemStack);
                    } catch (IllegalArgumentException e) {
                        log.warning(logPrefix + "Invalid alias in group " + g);
                        this.groups.remove(g);
                    }
                    if (this.groups.get(g) != null) this.groups.get(g).type(groups.getString(g + ".type"));
                }
                if (this.groups.get(g) != null) log.info(logPrefix + "Group " + g + " (" + this.groups.get(g).type() + ") added.");
            }
        }
        configuration.save();
    }

    /**
     * Gets all aliases for an item
     * @param query name of item
     * @return names of aliases
     * @throws IllegalArgumentException exception if no such item exists
     */
    public List<String> getAliases(String query) throws IllegalArgumentException {
        List<String> s = new ArrayList<String>();
        ItemStack i = itemMap.get(query);
        if (i == null)
            throw new IllegalArgumentException("no such item");
        String b = Integer.toString(i.getTypeId());
        int d = i.getDurability();
        if (d != 0)
            b += ";" + Integer.toString(i.getDurability());
        if (items.get(b) != null)
            s.addAll(items.get(b));
        if (d == 0 && items.get(b + ";0") != null)
            s.addAll(items.get(b + ";0"));
        return s;
    }

    /**
     * Returns all group names
     * @return list of all groups
     */
    public List<String> getGroups() {
        return getGroups(null);
    }

    /**
     * Returns all group names starting with a string
     * @param group name to look for
     * @return list of matching groups
     */
    public List<String> getGroups(String group) {
        List<String> groups = new ArrayList<String>();
        Set<String> gs = this.groups.keySet();
        for (String g : gs) {
            if (group == null || (g.length() >= group.length() && g.regionMatches(true, 0, group, 0, group.length())))
                groups.add(g);
        }
        return groups;
    }

    /**
     * Returns list of all items in a group as "#;#" (id;durability)
     * @param query item group name
     * @return list of items
     * @throws IllegalArgumentException exception if no such group exists
     */
    public List<String> getItemGroupNames(String query) throws IllegalArgumentException {
        ArrayList<String> names = groups.keySet();
        if (names == null)
            throw new IllegalArgumentException("no such group");
        return names;
    }

    /**
     * Returns list of ItemStack for items in a group, all quantity 1
     * @param query item group name
     * @return list of ItemStack
     * @throws IllegalArgumentException exception if no such group exists
     */
    public List<ItemStack> getItemGroup(String query) throws IllegalArgumentException {
        return getItemGroup(query, 1);
    }

    /**
     * Returns list of ItemStack for items in a group with specific quantity per ItemStack
     * @param query item group name
     * @param quantity quantity for ItemStack
     * @return list of ItemStack
     * @throws IllegalArgumentException exception if no such group exists or quantity list doesn't match group length
     */
    public List<ItemStack> getItemGroup(String query, List<Integer> quantity) throws IllegalArgumentException {
        List<ItemStack> i = new ArrayList<ItemStack>();
        List<String> g = groups.get(query);
        if (g == null)
            throw new IllegalArgumentException("no such group");
        if (quantity.size() != g.size())
            throw new IllegalPluginAccessException("non-matching quantity list and group length");
        ListIterator<Integer> q = quantity.listIterator();
        for (String x : g) {
            try {
                i.add(getItemStack(x, q.next()));
            } catch (IllegalArgumentException e) {
                log.severe(logPrefix + "Bad data in configuration of group \"" + query + "\"");
                log.severe(logPrefix + "Suggested correction for bad entry \"" + x + "\": " + e.getMessage());
            }
        }
        return i;
    }

    /**
     * Returns list of ItemStack for items in a group with specific quantity for all ItemStack
     * @param query item group name
     * @param quantity quantity for ItemStack, or -1 to use quantities from OddItem.yml
     * @return list of ItemStack
     * @throws IllegalArgumentException exception if no such group exists
     */
    public List<ItemStack> getItemGroup(String query, Integer quantity) throws IllegalArgumentException {
        List<ItemStack> i = new ArrayList<ItemStack>();
        List<String> g = groups.get(query);
        if (g == null)
            throw new IllegalArgumentException("no such group");
        for (String x : g) {
            String a = x;
            Integer q = quantity;
            try {
                if (x.contains(":")) {
                    a  = x.substring(0, x.indexOf(":"));
                    try {
                       if (quantity == -1)
                           q = Integer.parseInt(x.substring(x.indexOf(":") + 1));
                    } catch (NumberFormatException e) {
                        q = 1;
                        log.warning(logPrefix + "Bad or no quantity in configuration of group \"" + query + "\" for \"" + query + "\", default to 1");
                    }
                }
                i.add(getItemStack(a, q));
            } catch (IllegalArgumentException e) {
                log.severe(logPrefix + "Invalid alias in configuration of group \"" + query + "\"");
                log.severe(logPrefix + "Suggested correction: \"" + x + "\": " + e.getMessage());
            }
        }
        return i;
    }

    /**
     * Returns an ItemStack of quantity 1 of alias query
     * @param query item name
     * @return ItemStack
     * @throws IllegalArgumentException exception if item not found, message contains closest match
     */
    public ItemStack getItemStack(String query) throws IllegalArgumentException {
        return getItemStack(query, 1);
    }

    /**
     * Returns an ItemStack of specific quantity of alias query
     * @param query item name
     * @param quantity quantity
     * @return ItemStack
     * @throws IllegalArgumentException exception if item not found, message contains closest match
     */
    public ItemStack getItemStack(String query, Integer quantity) throws IllegalArgumentException {
        ItemStack i = itemMap.get(query);
        if (i != null) {
            i.setAmount(quantity);
            return i;
        }
        throw new IllegalArgumentException(bktree.findBestWordMatch(query));
    }

    @Override
    public void onDisable() {
        log.info(logPrefix + "disabled");
    }

    @Override
    public void onEnable() {
        Plugin p = getServer().getPluginManager().getPlugin("Permissions");
        if (p != null)
            ph = ((Permissions) p).getHandler();
        log.info(logPrefix + info.getVersion() + " enabled");
        getCommand("odditem").setExecutor(new OddItemCommandExecutor(this));
        itemMap = new ConcurrentHashMap<String, ItemStack>();
        items = new ConcurrentSkipListMap<String, SortedSet<String>>(String.CASE_INSENSITIVE_ORDER);
        groups = new ConcurrentHashMap<String, LinkedList<String>>();
        configure();
        log.info(logPrefix + itemMap.size() + " aliases loaded.");
    }

    @Override
    public void onLoad() {
        info = getDescription();
        log = getServer().getLogger();
        logPrefix = "[" + info.getName() + "] ";
    }

    private void writeConfig() {
        FileWriter fw;
        try {
            fw = new FileWriter(configurationFile);
        } catch (IOException e) {
            log.severe(logPrefix + "Couldn't write config file: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        BufferedReader i = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/OddItem.yml")));
        BufferedWriter o = new BufferedWriter(fw);
        try {
            String line = i.readLine();
            while (line != null) {
                o.write(line + System.getProperty("line.separator"));
                line = i.readLine();
            }
            log.info(logPrefix + "Wrote default config");
        } catch (IOException e) {
            log.severe(logPrefix + "Error writing config: " + e.getMessage());
        } finally {
            try {
                o.close();
                i.close();
            } catch (IOException e) {
                log.severe(logPrefix + "Error saving config: " + e.getMessage());
                getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    protected void save() {

    }

    protected boolean uglyPermission(Player player, String permission) {
        if (permission.equals("yeti")) {
            return ph.has(player, permission);
        }
        return player.hasPermission(permission);
    }
}