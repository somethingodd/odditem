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

import info.somethingodd.bukkit.OddItem.OddItemConfiguration;
import info.somethingodd.bukkit.OddItem.bktree.BKTree;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemAliases implements ConfigurationSerializable {
    private BKTree<String> suggestions;
    private Map<String, ItemStack> items;
    private Map<String, Set<String>> aliases;

    public OddItemAliases(Map<String, Object> serialized) {
        suggestions = new BKTree<String>(OddItemConfiguration.getComparator());
        items = new TreeMap<String, ItemStack>(String.CASE_INSENSITIVE_ORDER);
        aliases = new TreeMap<String, Set<String>>(String.CASE_INSENSITIVE_ORDER);
        for (String key : serialized.keySet()) {
            ItemStack itemStack = stringToItemStack(key);
            Object value = serialized.get(key);
            Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
            names.addAll((Collection<String>) value);
            for (String alias : names) {
                aliases.put(alias, names);
                items.put(alias, itemStack);
                suggestions.add(alias);
            }
        }
    }

    private String itemStackToString(ItemStack itemStack) {
        return new StringBuilder().append(itemStack.getTypeId() + ";" + itemStack.getDurability()).toString();
    }

    private ItemStack stringToItemStack(String string) {
        int typeId;
        short damage = 0;
        if (string.contains(";")) {
            typeId = Integer.valueOf(string.substring(0, string.indexOf(";")));
            damage = Short.valueOf(string.substring(string.indexOf(";") + 1));
        } else {
            typeId = Integer.valueOf(string);
        }
        return new ItemStack(typeId, 1, damage);
    }

    public Map<String, Set<String>> getAliases() {
        return Collections.synchronizedMap(Collections.unmodifiableMap(aliases));
    }

    public Map<String, ItemStack> getItems() {
        return Collections.synchronizedMap(Collections.unmodifiableMap(items));
    }

    public BKTree<String> getSuggestions() {
        return suggestions;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> reverseItems = new TreeMap<String,Object>(String.CASE_INSENSITIVE_ORDER);
        for (String alias : aliases.keySet()) {
            if (reverseItems.containsKey(itemStackToString(items.get(alias))))
                continue;
            List<String> names = new ArrayList<String>(aliases.get(alias));
            reverseItems.put(itemStackToString(items.get(alias)), names);
        }
        return reverseItems;
    }

    public static OddItemAliases deserialize(Map<String, Object> serialized) {
        return new OddItemAliases(serialized);
    }

    public static OddItemAliases valueOf(Map<String, Object> serialized) {
        return new OddItemAliases(serialized);
    }

    public int hashCode() {
        int hash = 17;
        hash += suggestions.hashCode();
        hash += items.hashCode();
        hash += aliases.hashCode();
        return hash;
    }

    public boolean equals(Object other) {
        if (!(other instanceof OddItemAliases)) return false;
        if (this == other) return true;
        if (!getItems().equals(((OddItemAliases) other).getItems())) return false;
        if (!getAliases().equals(((OddItemAliases) other).getAliases())) return false;
        return true;
    }
}