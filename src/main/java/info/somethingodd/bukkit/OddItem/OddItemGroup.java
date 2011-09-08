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

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.config.ConfigurationNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemGroup implements Iterable<ItemStack> {
    private ConfigurationNode data = null;
    private Iterator<ItemStack> iterator = null;
    private List<ItemStack> items = null;

    public OddItemGroup(List<ItemStack> i, ConfigurationNode data) {
        items = Collections.synchronizedList(new ArrayList<ItemStack>());
        for (ItemStack is : i)
            items.add(is);
        this.data = data;
    }

    public ConfigurationNode getData() {
        return data;
    }

    public Iterator<ItemStack> iterator() {
        if (items == null) items = Collections.synchronizedList(new ArrayList<ItemStack>());
        if (iterator == null) iterator = items.iterator();
        return iterator;
    }

    public int size() {
        if (items != null) return items.size();
        return 0;
    }

    /**
     * Returns whether group contains ItemStack
     * @param is ItemStack to look for
     * @return boolean group contains ItemStack
     */
    public boolean contains(ItemStack is) {
        return contains(is, false);
    }

    /**
     * Returns whether group contains ItemStack
     * @param is ItemStack to look for
     * @param quantity boolean whether to check quantity of ItemStack
     * @return boolean group contains ItemStack
     */
    public boolean contains(ItemStack is, boolean quantity) {
        return contains(is, true, quantity);
    }

    /**
     * Returns whether group contains ItemStack
     * @param is ItemStack to look for
     * @param durability boolean whether to check durability of ItemStack
     * @param quantity boolean whether to check quantity of ItemStack
     * @return boolean group contains ItemStack
     */
    public boolean contains(ItemStack is, boolean durability, boolean quantity) {
        if (items == null) return false;
        for (ItemStack i : items)
            if (OddItem.compare(is, i, durability, quantity)) return true;
        return false;
    }

    public ItemStack get(int index) throws IndexOutOfBoundsException {
        return items.get(index);
    }

    public boolean equals(ItemStack i) {
        if (items == null) return false;
        if (items.size() != 1) return false;
        if (items.get(0).getTypeId() != i.getTypeId()) return false;
        if (items.get(0).getDurability() != i.getDurability()) return false;
        if (items.get(0).getAmount() != i.getAmount()) return false;
        return true;
    }
}
