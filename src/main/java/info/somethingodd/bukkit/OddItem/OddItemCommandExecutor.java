package info.somethingodd.bukkit.OddItem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OddItemCommandExecutor implements CommandExecutor {

    OddItem oddItem = null;

    public OddItemCommandExecutor(OddItem oddItem) {
        this.oddItem = oddItem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp() && !oddItem.uglyPermission((Player) sender, "odditem")) {
            sender.sendMessage("Not allowed.");
            return true;
        }
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
        }
        return false;
    }
}
