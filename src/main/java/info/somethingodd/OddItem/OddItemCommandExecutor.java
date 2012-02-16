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
package info.somethingodd.OddItem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemCommandExecutor implements CommandExecutor {
    private OddItemBase oddItemBase = null;

    public OddItemCommandExecutor(OddItemBase oddItemBase) {
        this.oddItemBase = oddItemBase;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("odditem")) {
            if (sender.hasPermission("odditem.alias")) {
                switch (args.length) {
                    case 0:
                        if (sender instanceof Player) {
                            sender.sendMessage(OddItem.getAliases(((Player) sender).getItemInHand()).toString());
                            return true;
                        }
                        break;
                    case 1:
                        try {
                            sender.sendMessage(OddItem.getAliases(args[0]).toString());
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage("[OddItem] No such alias. Similar: " + e.getMessage());
                        }
                        return true;
                }
            } else {
                sender.sendMessage("DENIED");
            }
        } else if (command.getName().equals("odditeminfo")) {
            if (sender.hasPermission("odditem.info")) {
                sender.sendMessage("[OddItem] " + OddItem.items.getAliases().size() + " items with " + OddItem.items.getItems().size() + " aliases");
            } else {
                sender.sendMessage("DENIED");
            }
            return true;
        } else if (command.getName().equals("odditemreload")) {
            if (sender.hasPermission("odditem.reload")) {
                sender.sendMessage("[OddItem] Reloading...");
                new OddItemConfiguration(oddItemBase).configure();
            } else {
                sender.sendMessage("DENIED");
            }
            return true;
        }
        return false;
    }
}
