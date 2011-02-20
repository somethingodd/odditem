package info.somethingodd.bukkit.odd.item;

import info.somethingodd.bukkit.odd.item.bktree.BKTree;
import info.somethingodd.bukkit.odd.item.bktree.LevenshteinDistance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

public class OddItem extends JavaPlugin {
	public static HashMap<String, String> items;
	public static String[] bkTreeKeys;
	public static BKTree<String> tree = null;
	public static Logger log;
	public static Server server;
	public static Plugin plugin;
	public static PluginDescriptionFile info;
	public static final String dataDir = "plugins" + File.separator + "Odd";
	public static final String config = dataDir + File.separator + "item.txt";

	public OddItem(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
		super(pluginLoader, instance, desc, folder, plugin, cLoader);
		server = instance;
		info = desc;
		OddItem.plugin = this;
	}

	private static String findItem(String search) {
		if (tree == null)
			getTree();
		return tree.findBestWordMatch(search);
	}

	public static ItemStack getItemStack(String m) throws IllegalArgumentException {
		Material material = null;
		ItemStack is = null;
		short damage = 0;
		if (material == null) {
			try {
				material = Material.getMaterial(Integer.decode(m));
			} catch (NumberFormatException nfe) {
				material = Material.getMaterial(m.toUpperCase());
			}
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
				IllegalArgumentException ex = new IllegalArgumentException(mat);
				throw ex;
			}
		}
		is = new ItemStack(material, 1, damage);
		return is;
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
				return true;
			}
			if (args.length >= 1) {
				if (args[0].equals("info")) {
					sender.sendMessage("[" + info.getName() + "] " + items.size() + " entries currently loaded.");
					return true;
				} else if (args[0].equals("reload")) {
					getItems();
					getTree();
					sender.sendMessage("[" + info.getName() + "] " + items.size() + " entries loaded.");
					return true;
				} else if (args[0].equals("list")) {
					sender.sendMessage(items.toString());
					if (args.length == 2) {
						sender.sendMessage(items.get(args[1]));
					}
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public void onDisable() {
		log.info( "[" + info.getName() + "] disabled" );
	}

	public void onEnable() {
		info = this.getDescription();
		log = Logger.getLogger("Minecraft");
		log.info( "[" + info.getName() + "] " + info.getVersion() + " enabled" );
		getItems();
	}

	public static HashMap<String, String> parseConfig(String s) {
		HashMap<String, String> it = new HashMap<String, String>();
		String[] l = s.split(System.getProperty("line.separator"));
		if (l.length > 0 && l[0].contains(":"))
			for (int x = 0; x < l.length; x++) {
				if (l[x] != "") {
					String[] i = l[x].split(":");
					String[] n = null;
					String m;
					if (i[0].contains("|")) {
						n = i[0].split("\\|");
						m = i[1];
					} else {
						n = i[1].split("\\|");
						m = i[0];
					}
					for (int y = 0; y < n.length; y++) {
						it.put(n[y], m);
					}
				}
			}
		log.info( "[" + info.getName() + "] Parsed " + it.size() + " entries.");
		return it;
	}

	public static String readConfig() {
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
			} catch (IOException ie) {
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
