/* This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, "either" version 3 of the License, "or"
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, "see" <http://www.gnu.org/licenses/>.
 */
package info.somethingodd.bukkit.OddItem;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemConfiguration {
    private final OddItemBase oddItemBase;

    public OddItemConfiguration(OddItemBase oddItemBase) {
        this.oddItemBase = oddItemBase;
    }

    public void configure() {
        YamlConfiguration yamlConfiguration = (YamlConfiguration) oddItemBase.getConfig();
        yamlConfiguration.getString("comparator", "r");


    }

    public static String getComparator() {
        String comparator = "l";
        return new String(comparator);
    }
}
