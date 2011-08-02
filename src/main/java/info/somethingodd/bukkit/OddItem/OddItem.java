package info.somethingodd.bukkit.OddItem;

import info.somethingodd.bukkit.OddItem.bktree.BKTree;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class OddItem extends JavaPlugin {
    protected ConcurrentMap<String, ItemStack> itemMap;
	protected ConcurrentNavigableMap<String, SortedSet<String>> items;
    protected ConcurrentNavigableMap<String, SortedSet<String>> groups;
	private BKTree<String> bktree = null;
	private Logger log = null;
	private PluginDescriptionFile info;
    private final String configurationFile = "plugins" + File.separator + "OddItem.yml";
    protected String logPrefix;

    protected void configure() {
        File configurationFile = new File(this.configurationFile);
        if (!configurationFile.exists())
            writeConfig();
        Configuration configuration = new Configuration(configurationFile);
        configuration.load();
        String comparator = configuration.getString("comparator");
        if (comparator != null) {
            if (false) {
            } else if (comparator.equalsIgnoreCase("c") || comparator.equalsIgnoreCase("caverphone")) {
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
                this.items.put(i, new ConcurrentSkipListSet<String>(new OddItemAliasComparator()));
            ArrayList<String> j = new ArrayList<String>();
            j.addAll(configuration.getStringList("items." + i, new ArrayList<String>()));
            this.items.get(i).addAll(j);
            Integer id = 0;
            Short d = 0;
            Material m = null;
            if (i.contains(";")) {
                try {
                    id = Integer.parseInt(i.substring(0, i.indexOf(";")));
                    d = Short.parseShort(i.substring(i.indexOf(";") + 1));
                    m = Material.getMaterial(id);
                } catch (NumberFormatException e) {
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
        for (String g : groups.getKeys()) {
            if (this.groups.get(g) == null)
                this.groups.put(g, new ConcurrentSkipListSet<String>());
        }
    }

    public Set<String> getAliases(String query) {
        Set<String> s = new HashSet<String>();
        ItemStack i = itemMap.get(query);
        if (i == null)
            return null;
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

    public ItemStack[] getItemGroup(String query) {
        return getItemGroup(query, 1);
    }

    public ItemStack[] getItemGroup(String query, Integer quantity) {
        SortedSet<ItemStack> i = new ConcurrentSkipListSet<ItemStack>(new OddItemGroupComparator());
        for (String x : groups.get(query)) {
            try {
                i.add(getItemStack(query, quantity));
            } catch (IllegalArgumentException e) {
                log.severe(logPrefix + "Bad data in configuration of group \"" + query + "\"");
                log.severe(logPrefix + "Suggested correction for bad entry \"" + x + "\": " + e.getMessage());
            }
        }
        return (ItemStack[]) i.toArray();
    }

    public ItemStack getItemStack(String query) throws IllegalArgumentException {
        return getItemStack(query, 1);
    }

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
        log.info(logPrefix + info.getVersion() + " enabled");
        getCommand("odditem").setExecutor(new OddItemCommand(this));
        itemMap = new ConcurrentHashMap<String, ItemStack>();
        items = new ConcurrentSkipListMap<String, SortedSet<String>>(new OddItemIDComparator());
        groups = new ConcurrentSkipListMap<String, SortedSet<String>>(new OddItemGroupComparator());
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
        BufferedReader i = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/OddItem.yml")));;
        BufferedWriter o = new BufferedWriter(fw);
        try {
            String line = i.readLine();
            while (line != null) {
                o.write(line + System.getProperty("line.separator"));
                line = i.readLine();
            }
        } catch (IOException e) {
            log.severe(logPrefix + "Error writing config: " + e.getMessage());
        } finally {
            try {
                o.close();
                i.close();
            } catch (IOException e) {
                log.severe(logPrefix + "Error saving config: " + e.getMessage());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }
        log.info(logPrefix + "Wrote default config");
    }

}