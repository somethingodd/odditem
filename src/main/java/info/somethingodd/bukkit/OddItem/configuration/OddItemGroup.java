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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemGroup implements ConfigurationSerializable, Iterable<ItemStack> {
    private List<ItemStack> itemStacks;
    private List<String> items;
    private Map<String, Object> data;

    public OddItemGroup(Map<String, Object> serialized) {
        data = ((ConfigurationSection) serialized.get("data")).getValues(false);
        items = (List<String>) serialized.get("items");
        itemStacks = new ArrayList<ItemStack>();
        for (String item : items) {
            ItemStack itemStack;
            try {
                if (item.contains(",")) {
                    itemStack = OddItem.getItemStack(item.substring(0, item.indexOf(",")));
                    itemStack.setAmount(Integer.valueOf(item.substring(item.indexOf(",") + 1)));
                } else {
                    itemStack = OddItem.getItemStack(item);
                    itemStack.setAmount(1);
                }
                itemStacks.add(itemStack);
            } catch (IllegalArgumentException e2) {
                Bukkit.getLogger().warning("Invalid item \"" + item + "\" in groups.yml (" + items.toString() + ")");
            }
        }
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public boolean match(String key) {
        for (String k : data.keySet())
            if (k.equals(key))
                return true;
        return false;
    }

    public boolean match(String key, String key2) {
        for (Object x : data.values()) {
            if (x instanceof MemorySection)
                return ((MemorySection) x).getValues(false).containsKey("key2");
            if (x instanceof Map)
                return ((Map) x).containsKey(key2);
            if (x instanceof List)
                return ((List) x).contains(key2);
        }
        return false;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("OddItemGroup");
        str.append("{");
        str.append("items=").append(items.toString());
        str.append(",");
        str.append("data=").append(data.toString());
        str.append("}\n");
        return str.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new TreeMap<String, Object>(OddItem.ALPHANUM_COMPARATOR);
        serialized.put("items", items);
        serialized.put("data", data);
        return serialized;
    }

    public static OddItemGroup deserialize(Map<String, Object> serialized) {
        return new OddItemGroup(serialized);
    }

    public static OddItemGroup valueOf(Map<String, Object> serialized) {
        return new OddItemGroup(serialized);
    }

    public ItemStack get(int index) {
        return itemStacks.get(index);
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return Collections.unmodifiableList(itemStacks).iterator();
    }
}
