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
import java.util.HashMap;
import java.util.logging.Logger;

public class OddItem extends JavaPlugin {
	private static HashMap<String, String> items;
	private static BKTree<String> tree = null;
	private static Logger log;
	private static PluginDescriptionFile info;
    private PermissionHandler Permissions = null;
	private static final String dataDir = "plugins" + File.separator + "Odd";
	private static final String config = dataDir + File.separator + "item.txt";

	private static String findItem(String search) {
		if (tree == null)
			getTree();
		return tree.findBestWordMatch(search);
	}

	public static ItemStack getItemStack(String m) throws IllegalArgumentException {
		Material material = null;
		short damage = 0;
		try {
			material = Material.getMaterial(Integer.decode(m));
		} catch (NumberFormatException nfe) {
			material = Material.getMaterial(m.toUpperCase());
		}
		if (material == null) {
			try {
				String i = items.get(m);
				String item = i;
				if (i != null) {
					if (i.contains(";")) {
							try {
								damage = Short.decode(i.split(";")[1]);
							} catch (NumberFormatException nfe) {
								log.warning("[" + info.getName() + "] Bad item damage value for " + i);
							}
							item = i.split(";")[0];
					}
					try {
						material = Material.getMaterial(Integer.decode(item));
					} catch (NumberFormatException nfe) {
						material = Material.getMaterial(item.toUpperCase());
					}
				}
				if (material == null) {
					log.info("[" + info.getName() + "] Item " + m + " not found.");
					throw new IllegalArgumentException(m);
				}
			} catch (IllegalArgumentException iae) {
				String mat = findItem(iae.getMessage());
				throw new IllegalArgumentException(mat);
			}
		}
		return new ItemStack(material, 1, damage);
	}

	private static void getItems() {
		String config;
		config = readConfig();
		items = parseConfig(config);
	}

	private static void getTree() {
		tree = new BKTree<String>(new LevenshteinDistance());
		for (String item : items.keySet()) {
			tree.add(item);
		}
	}

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
            if (args[0].equals("info")) {
                sender.sendMessage("[" + info.getName() + "] " + items.size() + " entries currently loaded.");
                return true;
            }
            if (args[0].equals("reload")) {
                getItems();
                getTree();
                sender.sendMessage("[" + info.getName() + "] " + items.size() + " entries loaded.");
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

	public void onDisable() {
		log.info( "[" + info.getName() + "] disabled" );
	}

	public void onEnable() {
		info = this.getDescription();
		log = getServer().getLogger();
		log.info( "[" + info.getName() + "] " + info.getVersion() + " enabled" );
        setupPermissions();
		getItems();
	}

	private static HashMap<String, String> parseConfig(String s) {
		HashMap<String, String> it = new HashMap<String, String>();
		String[] l = s.split(System.getProperty("line.separator"));
		if (l.length > 0 && l[0].contains(":"))
            for (String aL : l) {
                if (!aL.equals("")) {
                    String[] i = aL.split(":");
                    String[] n = null;
                    String m;
                    if (i[0].contains("|")) {
                        n = i[0].split("\\|");
                        m = i[1];
                    } else {
                        n = i[1].split("\\|");
                        m = i[0];
                    }
                    for (String aN : n) {
                        it.put(aN, m);
                    }
                }
            }
		log.info( "[" + info.getName() + "] Parsed " + it.size() + " entries.");
		return it;
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
                log.warning("Reading config: " + ioe.getMessage());
			} finally {
				input.close();
			}
		}
		catch (IOException ie){
			log.severe(ie.getMessage());
		}
		return contents.toString();
	}

    public void setupPermissions() {
        Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
        if (Permissions == null && test != null) {
                this.getServer().getPluginManager().enablePlugin(test);
                Permissions = ((Permissions) test).getHandler();
        } else {
            log.info("[" + info.getName() + "] Permissions not found. Op-only mode.");
        }
    }
}
