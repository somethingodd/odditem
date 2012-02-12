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
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemGroup implements ConfigurationSerializable {
    private final List<ItemStack> items;
    private final Map<String, Map<String, List<String>>> data;

    public OddItemGroup(List<ItemStack> items, Map<String, Map<String, List<String>>> data) {
        this.items = items;
        this.data = data;
    }

    public OddItemGroup(List<String> items, Map<String, Map<String, List<String>>> data, boolean b) {
        this.items = new ArrayList<ItemStack>();
        for (String item : items) {
            ItemStack itemStack;
            int quantity = 1;
            if (item.contains(",")) {
                itemStack = OddItem.getItemStack(item.substring(0, item.indexOf(",")));
                quantity = Integer.valueOf(item.substring(item.indexOf(",") + 1));
            } else {
                itemStack = OddItem.getItemStack(item);
            }
            itemStack.setAmount(quantity);
            this.items.add(itemStack);
        }
        this.data = data;
    }

    public OddItemGroup(ItemStack... itemStacks) {
        List<ItemStack> items = new ArrayList<ItemStack>();
        for (ItemStack itemStack : itemStacks)
            items.add(itemStack);
        this.items = items;
        this.data = new TreeMap<String, Map<String, List<String>>>(String.CASE_INSENSITIVE_ORDER);
    }

    public OddItemGroup(Map<String, Object> serialized) {
        data = new TreeMap<String, Map<String, List<String>>>(String.CASE_INSENSITIVE_ORDER);
        Map<String, Map<String, List<String>>> datasrc = (Map<String, Map<String, List<String>>>) serialized.get("data");
        for (String datum : datasrc.keySet())
            data.put(datum, datasrc.get(datum));
        items = new ArrayList<ItemStack>();
        try {
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
                    Bukkit.getLogger().warning("[OddItem] Couldn't find item \"" + item + "\" in groups.yml");
                }
            }
        } catch (ClassCastException e) {
            for (ItemStack item : (Collection<ItemStack>) serialized.get("items")) {
                items.add(item);
            }
        }
    }

    public Map<String, Map<String, List<String>>> getData() {
        return Collections.unmodifiableMap(data);
    }

    public Map<String, List<String>> getData(String group) {
        return data.get(group);
    }

    public void setData(String group, Map<String, List<String>> data) {
        this.data.put(group, data);
    }

    public void setData(String group, String key, List<String> data) {
        this.data.get(group).put(key, data);
    }

    public void addData(String group, String key, String data) {
        if (this.data.get(group).get(key) == null)
            this.data.get(group).put(key, new ArrayList<String>());
        this.data.get(group).get(key).add(data);
    }

    public void removeData(String group, String key, String data) {
        this.data.get(group).get(key).remove(data);
    }

    public void removeData(String group, String key) {
        this.data.get(group).remove(key);
    }

    public boolean contains(String group, String key) {
        return this.data.get(group).containsKey(key);
    }

    public Collection<ItemStack> getItems() {
        return items;
    }

    public String toString() {
        StringBuilder string = new StringBuilder("OddItemGroup{");
        string.append("items=").append(items.toString());
        string.append(", data=").append(data.toString());
        string.append("}");
        return string.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        serialized.put("data", data);
        serialized.put("items", items);
        return serialized;
    }

    public static OddItemGroup deserialize(Map<String, Object> serialized) {
        return new OddItemGroup(serialized);
    }

    public static OddItemGroup valueOf(Map<String, Object> serialized) {
        return new OddItemGroup(serialized);
    }
}
