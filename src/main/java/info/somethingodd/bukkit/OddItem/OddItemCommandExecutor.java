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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 1:
                if (args[0].equals("info") && (!(sender instanceof Player) || OddItem.uglyPermission((Player) sender, "odditem." + args[0]))) {
                    sender.sendMessage(OddItem.logPrefix + OddItem.itemMap.size() + " aliases loaded");
                    return true;
                } else if (args[0].equals("reload") && (!(sender instanceof Player) || OddItem.uglyPermission((Player) sender, "odditem." + args[0]))) {
                    OddItem.configure();
                }
                break;
            case 2:
                if (args[0].equals("aliases") || args[0].equals("alias") && (!(sender instanceof Player) || OddItem.uglyPermission((Player) sender, "odditem.alias"))) {
                    String message;
                    try {
                        message = OddItem.getAliases(args[1]).toString();
                    } catch (IllegalArgumentException e) {
                        message = "no such item";
                    }
                    sender.sendMessage(OddItem.logPrefix + message);
                    return true;
                }
                break;
        }
        sender.sendMessage("Invalid command.");
        return false;
    }
}
