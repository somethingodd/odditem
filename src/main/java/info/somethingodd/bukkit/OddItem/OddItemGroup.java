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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemGroup implements Iterable<ItemStack> {
    private ConfigurationSection data = null;
    private List<ItemStack> items = null;
    private String name = null;

    public OddItemGroup() {
    }

    public OddItemGroup(List<ItemStack> i, ConfigurationSection data) {
        items = Collections.synchronizedList(new ArrayList<ItemStack>());
        for (ItemStack is : i)
            items.add(is);
        this.data = data;
    }

    public ConfigurationSection getData() {
        return data;
    }

    public void setData(ConfigurationSection data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(ItemStack itemStack) {
        if (items == null) items = Collections.synchronizedList(new ArrayList<ItemStack>());
        items.add(itemStack);
    }

    public boolean remove(ItemStack itemStack) {
        return items.remove(itemStack);
    }

    public ItemStack remove(int index) {
        return items.remove(index);
    }

    @Override
    public Iterator<ItemStack> iterator() {
        if (items == null) items = Collections.synchronizedList(new ArrayList<ItemStack>());
        return items.iterator();
    }

    public int size() {
        if (items != null) return items.size();
        return 0;
    }

    /**
     * Returns whether group contains ItemStack matching material and durability, ignoring quantity
     * @param is ItemStack to look for
     * @return boolean group contains ItemStack
     */
    public boolean contains(ItemStack is) {
        return contains(is, false);
    }

    /**
     * Returns whether group contains ItemStack matching material and durability
     * @param is ItemStack to look for
     * @param quantity boolean whether to check quantity of ItemStack
     * @return boolean group contains ItemStack
     */
    public boolean contains(ItemStack is, boolean quantity) {
        return contains(is, true, quantity);
    }

    /**
     * Returns whether group contains ItemStack matching material
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
}
