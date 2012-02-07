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

import info.somethingodd.bukkit.OddItem.OddItem;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemGroup implements ConfigurationSerializable {
    private final String name;
    private final String plugin;
    private final List<ItemStack> items;
    private final YamlConfiguration data;

    public OddItemGroup(Map<String, Object> serialized) {
        plugin = (String) serialized.get("plugin");
        name = (String) serialized.get("name");
        data = new YamlConfiguration();
        data.set("data", serialized.get("data"));
        items = new ArrayList<ItemStack>();
        for (String item : (Collection<String>) serialized.get("items")) {
            try {
                ItemStack itemStack;
                int quantity = 1;
                if (item.contains(",")) {
                    itemStack = OddItem.getItemStack(item.substring(0, item.indexOf(",")));
                    quantity = Integer.valueOf(item.substring(item.indexOf(",") + 1));
                } else {
                    itemStack = OddItem.getItemStack(item);
                }
                itemStack.setAmount(quantity);
                items.add(itemStack);
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("[OddItem] Error loading group \"" + name + "\": Couldn't find item \"" + item + "\".");
            }
        }
    }

    public Object getData() {
        return data;
    }

    public Collection<ItemStack> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public String getPlugin() {
        return plugin;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        serialized.put("data", data);
        serialized.put("items", items);
        serialized.put("name", name);
        serialized.put("plugin", plugin);
        return serialized;
    }

    public OddItemGroup deserialize(Map<String, Object> serialized) {
        return new OddItemGroup(serialized);
    }

    public OddItemGroup valueOf(Map<String, Object> serialized) {
        return new OddItemGroup(serialized);
    }
}
