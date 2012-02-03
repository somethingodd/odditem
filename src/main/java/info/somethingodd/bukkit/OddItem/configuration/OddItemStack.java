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
package info.somethingodd.bukkit.OddItem.configuration;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemStack implements ConfigurationSerializable {
    private List<String> names;
    private ItemStack item;

    public OddItemStack(List<String> names, String id) {
        Collections.copy(this.names, names);
        short damage = 0;
        int typeId = 0;
        if (id.contains(";")) {
            damage = Short.valueOf(id.substring(id.indexOf(";"), id.length()));
            typeId = Integer.valueOf(id.substring(0, id.indexOf(";")));
        } else {
            typeId = Integer.valueOf(id);
        }
        item = new ItemStack(typeId, 1, damage);
    }

    public OddItemStack(List<String> names, int typeId, short damage) {
        Collections.copy(this.names, names);
        item = new ItemStack(typeId, 1, damage);
    }

    public short getDurability() {
        return item.getDurability();
    }

    public int getTypeId() {
        return item.getTypeId();
    }

    public Material getType() {
        return item.getType();
    }

    public String name() {
        return new StringBuilder().append(getTypeId()).append(";").append(getDurability()).toString();
    }

    public List<String> getAliases() {
        return Collections.unmodifiableList(names);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<String, Object>();
        serialized.put(name(), names);
        return serialized;
    }
}
