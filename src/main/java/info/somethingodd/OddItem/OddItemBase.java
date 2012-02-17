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

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemBase extends JavaPlugin {
    protected Logger log;

    @Override
    public void onDisable() {
        log.info("disabled");
        OddItem.clear();
        log = null;
    }

    @Override
    public void onEnable() {
        log = getLogger();
        log.info(getDescription().getVersion() + " enabled");
        try {
            OddItemConfiguration configuration = new OddItemConfiguration(this);
            configuration.configure();
        } catch (Exception e) {
            log.severe("Configuration error!");
            e.printStackTrace();
        }
        getCommand("odditem").setExecutor(new OddItemCommandExecutor(this));
        log.info(OddItem.items.getAliases().size() + " items with " + OddItem.items.getItems().size() + " aliases loaded.");
    }
}