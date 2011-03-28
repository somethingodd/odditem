package info.somethingodd.bukkit.OddItem;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import info.somethingodd.bukkit.OddItem.bktree.BKTree;
import info.somethingodd.bukkit.OddItem.bktree.LevenshteinDistance;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class OddItem extends JavaPlugin {
	private static final ConcurrentMap<String, String> items = new ConcurrentHashMap<String, String>();
    private static final ConcurrentMap<String, HashSet<String>> aliases = new ConcurrentHashMap<String, HashSet<String>>();
	private static BKTree<String> tree = null;
	private static Logger log;
	private static PluginDescriptionFile info;
    private PermissionHandler Permissions = null;
	private static final String dataDir = "plugins" + File.separator + "Odd";
	private static final String config = dataDir + File.separator + "item.txt";
    private static String logPrefix;

    private static String findItem(String search) {
		if (tree == null)
			getTree();
		return tree.findBestWordMatch(search);
	}

    /*
     * Gets all configured aliases for a specified alias
     *
     * @param String item alias to lookup
     */
    public HashSet<String> getAliases(String i) {
        if (aliases.get(i) == null)
            return new HashSet<String>();
        return aliases.get(i);
    }

    /*
     * Gets an item stack of specified alias
     *
     * @param String m item alias to search for
     */
    public ItemStack getItemStack(String m) throws IllegalArgumentException {
        Material material;
        short damage = 0;
        String i = items.get(m);
        if (i == null && aliases.get(m) != null) {
            i = items.get(aliases.get(m).toArray()[0].toString());
        }
        String item;
        if (i != null) {
            if (i.contains(";")) {
                try {
                    damage = Short.decode(i.split(";")[1]);
                } catch (NumberFormatException nfe) {
                    log.warning(logPrefix + "Bad item damage value for " + i);
                    return null;
                }
                item = i.split(";")[0];
            } else {
                item = i;
            }
        } else {
            throw new IllegalArgumentException(findItem(m));
        }
        try {
            material = Material.getMaterial(Integer.decode(item));
        } catch (NumberFormatException nfe) {
            material = Material.getMaterial(Integer.decode(item));
        }
        return new ItemStack(material, 1, damage);
	}

    private static void getItems() {
		String config;
		config = readConfig();
		parseConfig(config);
	}

	private static void getTree() {
		tree = new BKTree<String>(new LevenshteinDistance());
		for (String item : items.keySet()) {
			tree.add(item);
		}
	}

    @Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (commandLabel.toLowerCase().equals("odditem")) {
            if (!sender.isOp()) {
                if (Permissions == null)
                    return true;
                if (args.length > 0 && !Permissions.has((Player) sender, "odd.item."+args[0]))
                    return true;
            }
            if (args.length == 0)
                return false;
            if (args[0].equals("aliases")) {
                if (args.length != 2)
                    return false;
                if (aliases.get(args[1]) != null)
                    sender.sendMessage(aliases.get(args[1]).toString());
                else
                    sender.sendMessage("Doesn't exist.");
                return true;
            }
            if (args[0].equals("info")) {
                sender.sendMessage(logPrefix + items.size() + " entries currently loaded.");
                return true;
            }
            if (args[0].equals("reload")) {
                getItems();
                getTree();
                sender.sendMessage(logPrefix + items.size() + " entries loaded.");
                return true;
            }
            if (args[0].equals("list")) {
                sender.sendMessage(items.toString());
                if (args.length == 2) {
                    sender.sendMessage(items.get(args[1]));
                }
                return true;
            }
        }
        return false;
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
        getItems();
	}

    @Override
    public void onLoad() {
        info = getDescription();
        log = getServer().getLogger();
        logPrefix = "[" + info.getName() + "] ";
    }

	private static void parseConfig(String s) {
        items.clear();
		String[] l = s.split(System.getProperty("line.separator"));
		if (l.length > 0 && l[0].contains(":"))
            for (String aL : l) {
                if (!aL.equals("")) {
                    String[] i = aL.split(":");
                    String[] n;
                    String m;
                    if (i[0].contains("|")) {
                        n = i[0].split("\\|");
                        m = i[1];
                    } else {
                        n = i[1].split("\\|");
                        m = i[0];
                    }
                    for (String aN : n) {
                        items.put(aN, m);
                        for (String aN2 : n) {
                            if (aliases.get(aN) == null) {
                                aliases.put(aN, new HashSet<String>());
                                aliases.get(aN).add(m);
                            }
                            if (!aN.equals(aN2))
                                aliases.get(aN).add(aN2);
                        }
                        if (aliases.get(m) == null)
                            aliases.put(m, new HashSet<String>());
                        aliases.get(m).add(aN);
                    }
                }
            }
		log.info(logPrefix + "Parsed " + items.size() + " entries.");
	}

	private static String readConfig() {
		boolean dirExists = new File(dataDir).exists();
		if (!dirExists) {
			try {
				new File(dataDir).mkdir(); 
			} catch (SecurityException se) {
				log.severe(se.getMessage());
				return null;
			}
		}
		boolean fileExists = new File(config).exists();
		if (!fileExists) {
			try {
				new File(config).createNewFile();
			} catch (IOException ioe) {
				log.severe(ioe.getMessage());
				return null;
			}
		}
		File file = new File(config);
		StringBuilder contents = new StringBuilder();
		try {
			BufferedReader input =  new BufferedReader(new FileReader(file));
			try {
				String line = input.readLine();
				while (line != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
					line = input.readLine();
				}
			} catch (IOException ioe) {
                log.warning(logPrefix + "Error reading config: " + ioe.getMessage());
			} finally {
				input.close();
			}
		}
		catch (IOException ie){
			log.severe(ie.getMessage());
		}
		return contents.toString();
	}
}
