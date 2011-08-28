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

    OddItem oddItem = null;

    public OddItemCommandExecutor(OddItem oddItem) {
        this.oddItem = oddItem;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !sender.isOp() && !oddItem.uglyPermission((Player) sender, "odditem."+args[0])) {
            sender.sendMessage("Invalid command.");
            return true;
        }
        switch (args.length) {
            case 1:
                if (args[0].equals("info")) {
                    sender.sendMessage(oddItem.logPrefix + oddItem.itemMap.size() + " aliases loaded");
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
                }
                break;
        }
        return false;
    }
}
