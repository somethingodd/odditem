package info.somethingodd.bukkit.OddItem;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import info.somethingodd.bukkit.OddItem.bktree.BKTree;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

public class OddItem extends JavaPlugin {
    protected static final ConcurrentMap<String, ItemStack> itemMap = new ConcurrentHashMap<String, ItemStack>();
	protected static final ConcurrentMap<String, ConcurrentSkipListSet<String>> items = new ConcurrentSkipListMap<String, ConcurrentSkipListSet<String>>();
	private static BKTree<String> bktree = null;
	private static Logger log;
	private static PluginDescriptionFile info;
    private PermissionHandler Permissions = null;
	private static final String configurationFile = "plugins" + File.separator + "OddItem.yml";
    protected static String logPrefix;
    private static Configuration configuration = null;

    protected void configure() {
        File configurationFile = new File(this.configurationFile);
        if (!configurationFile.exists())
            writeConfig();
        configuration = new Configuration(configurationFile);
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
                this.items.put(i, new ConcurrentSkipListSet<String>());
            ArrayList<String> j = new ArrayList<String>();
            j.addAll(configuration.getStringList("items." + i, new ArrayList<String>()));
            this.items.get(i).addAll(j);
            for (String item : j) {
                int id = 0;
                int damage = 0;
                if (i.contains(";")) {
                    id = Integer.decode(i.substring(0, i.indexOf(";")));
                    damage = Integer.decode(i.substring(i.indexOf(";") + 1));
                } else {
                    id = Integer.decode(i);
                }
                itemMap.put(item, new ItemStack(Material.getMaterial(id), damage));
                bktree.add(item);
            }
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

    public ItemStack getItemStack(String query) throws IllegalArgumentException {
        ItemStack i = itemMap.get(query);
        if (query != null)
            return i;
        throw new IllegalArgumentException(bktree.findBestWordMatch(query));
    }

    @Override
    public void onDisable() {
        log.info(logPrefix + "disabled");
    }

    @Override
    public void onEnable() {
        log.info(logPrefix + info.getVersion() + " enabled");
        Plugin p = getServer().getPluginManager().getPlugin("Permissions");
        if (p != null) {
            getServer().getPluginManager().enablePlugin(p);
            Permissions = ((Permissions) p).getHandler();
        } else {
            log.warning(logPrefix + "Permissions not found. Using op-only mode.");
        }
        getCommand("odditem").setExecutor(new OddItemCommand(this));
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
        try {
            BufferedReader i = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/OddItem.yml")));
            BufferedWriter o = new BufferedWriter(new FileWriter(configurationFile));
            String line = i.readLine();
            while (line != null) {
                o.write(line + System.getProperty("line.separator"));
                line = i.readLine();
            }
            o.close();
            i.close();
        } catch (Exception e) {
            log.severe(logPrefix + "Error writing configuration.");
            e.printStackTrace();
        } finally {
            log.info(logPrefix + "Wrote default config");
        }
    }

}