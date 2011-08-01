package info.somethingodd.bukkit.OddItem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class OddItemCommand implements CommandExecutor {

    OddItem oddItem = null;

    public OddItemCommand(OddItem oddItem) {
        this.oddItem = oddItem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                if (args[0].equals("info")) {
                    sender.sendMessage(oddItem.logPrefix + oddItem.itemMap.size() + " aliases loaded");
                    return true;
                }
                break;
            case 2:
                if (args[0].equals("aliases") || args[0].equals("alias")) {
                    Set<String> s = new HashSet<String>();
                    ItemStack i = null;
                    try {
                        i = oddItem.getItemStack(args[1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(oddItem.logPrefix + " Unknown item. Suggestion: " + e.getMessage());
                        return true;
                    }
                    String b = Integer.toString(i.getTypeId());
                    int d = i.getDurability();
                    if (d != 0)
                        b += ";" + Integer.toString(i.getDurability());
                    if (oddItem.items.get(b) != null)
                        s.addAll(oddItem.items.get(b));
                    if (d == 0 && oddItem.items.get(b + ";0") != null)
                        s.addAll(oddItem.items.get(b + ";0"));
                    sender.sendMessage(oddItem.logPrefix + s.toString());
                    return true;
                } else
                if (args[0].equals("remove")) {
                    try {
                        oddItem.getItemStack(args[1]);
                        oddItem.itemMap.remove(args[1]);
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(oddItem.logPrefix + " Unknown item. Suggestion: " + e.getMessage());
                    }
                    return true;
                }
                break;
            case 3:
                if (args[0].equals("add")) {
                    if (oddItem.itemMap.containsKey(args[1]) || oddItem.itemMap.containsKey(args[2])) {
                        sender.sendMessage(oddItem.logPrefix + " Already exists.");
                    } else {
                        int m = 0;
                        int d = 0;
                        if (args[0].contains(";") && !args[1].contains(";")) {
                            m = Integer.parseInt(args[1].substring(0, args[0].indexOf(";")));
                            d = Integer.parseInt(args[1].substring(args[0].indexOf(";") + 1));
                        } else {
                            m = Integer.parseInt(args[0]);
                        }
                        if (args[1].contains(";") && !args[0].contains(";")) {
                            m = Integer.parseInt(args[1].substring(0, args[1].indexOf(";")));
                            d = Integer.parseInt(args[1].substring(args[1].indexOf(";") + 1));
                        } else {
                            m = Integer.parseInt(args[1]);
                        }
                    }
                }

                break;
        }
        return false;
    }
}
