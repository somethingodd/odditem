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
package info.somethingodd.odditem;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemBase extends JavaPlugin {
    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisable() {
        OddItem.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        try {
            Configuration configuration = new Configuration(this);
            configuration.configure();
        } catch (Exception e) {
            getLogger().severe("Configuration error!");
            e.printStackTrace();
        }
        getCommand("odditem").setExecutor(new OddItemCommandExecutor(this));
        getLogger().info(OddItem.items.itemCount() + " items with " + OddItem.items.aliasCount() + " aliases loaded.");
    }
}
