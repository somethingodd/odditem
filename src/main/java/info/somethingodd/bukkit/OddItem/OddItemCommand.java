package info.somethingodd.bukkit.OddItem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
                } else if (args[0].equals("save")) {
                    oddItem.save();
                    return true;
                } else if (args[0].equals("groups")) {
                    String g = oddItem.groups.keySet().toString();
                    sender.sendMessage(oddItem.logPrefix + g);
                    return true;
                }
                break;
            case 2:
                if (args[0].equals("aliases") || args[0].equals("alias")) {
                    String message;
                    try {
                        message = oddItem.getAliases(args[1]).toString();
                    } catch (IllegalArgumentException e) {
                        message = "no such item";
                    }
                    sender.sendMessage(oddItem.logPrefix + message);
                    return true;
                } else if (args[0].equals("group")) {
                    String message;
                    try {
                        message = oddItem.getItemGroupNames(args[1]).toString();
                    } catch (IllegalArgumentException e) {
                        message = "no such group";
                    }
                    sender.sendMessage(oddItem.logPrefix + message);
                    return true;
                } else if (args[0].equals("remove")) {
                    try {
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
