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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemGroup {
    private String groupType = null;
    private Iterator<ItemStack> iterator = null;
    private List<ItemStack> items = null;

    public OddItemGroup() {
    }

    public String type() {
        return groupType;
    }

    public void type(String groupType) {
        this.groupType = groupType;
    }

    public void add(ItemStack i) {
        if (items == null) items = Collections.synchronizedList(new ArrayList<ItemStack>());
        items.add(i);
    }

    public void addAll(Collection<ItemStack> c) {
        if (items == null) items = Collections.synchronizedList(new ArrayList<ItemStack>());
        items.addAll(c);
    }

    public void clear() {
        items = null;
    }

    public int size() {
        if (items != null) return items.size();
        return 0;
    }

    public ItemStack get(int i) throws IndexOutOfBoundsException {
        if (items != null && size() > i) return items.get(i);
        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns whether group contains ItemStack, ignoring quantities
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
        if (items == null) return false;
        Iterator<ItemStack> i = items.iterator();
        while (i.hasNext())
            if (OddItem.compare(is, i.next(), quantity)) return true;
        return false;
    }

    public boolean hasNext() {
        if (items == null) items = Collections.synchronizedList(new ArrayList<ItemStack>());
        if (iterator == null) iterator = items.iterator();
        return iterator.hasNext();
    }

    public ItemStack next() throws NoSuchElementException {
        if (items == null) items = Collections.synchronizedList(new ArrayList<ItemStack>());
        if (iterator == null) iterator = items.iterator();
        return iterator.next();
    }
}
