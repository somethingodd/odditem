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

import info.somethingodd.OddItem.configuration.OddItemAliases;
import info.somethingodd.OddItem.configuration.OddItemGroup;
import info.somethingodd.OddItem.configuration.OddItemGroups;
import info.somethingodd.OddItem.util.AlphanumComparator;
import info.somethingodd.OddItem.util.ItemStackComparator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */

@Deprecated
public class OddItem {
    protected static OddItemAliases items = info.somethingodd.OddItem.OddItem.getItems();
    protected static OddItemGroups groups = info.somethingodd.OddItem.OddItem.getGroups();
    public static final AlphanumComparator ALPHANUM_COMPARATOR;
    public static final ItemStackComparator ITEM_STACK_COMPARATOR;

    static {
        ALPHANUM_COMPARATOR = info.somethingodd.OddItem.OddItem.ALPHANUM_COMPARATOR;
        ITEM_STACK_COMPARATOR = info.somethingodd.OddItem.OddItem.ITEM_STACK_COMPARATOR;
    }

    /**
     * Compares two Inventory for identical contents ignoring order
     *
     * @param a          first Inventory
     * @param b          second Inventory
     * @return Inventories are identical
     */
    public static boolean compare(Inventory a, Inventory b) {
        return info.somethingodd.OddItem.OddItem.compare(a, b, true);
    }

    /**
     * Compares two Inventory for identical contents ignoring order, and possibly ignoring quantity
     *
     * @param a          first Inventory
     * @param b          second Inventory
     * @param quantity   whether to check quantity
     * @return Inventories are identical
     */
    public static boolean compare(Inventory a, Inventory b, boolean quantity) {
        return info.somethingodd.OddItem.OddItem.compare(a, b, true, quantity);
    }

    /**
     * Compares two Inventory for identical contents ignoring order, and possibly ignoring durability and quantity
     *
     * @param a          first Inventory
     * @param b          second Inventory
     * @param durability whether to check durability
     * @param quantity   whether to check quantity
     * @return Inventories are identical
     */
    public static boolean compare(Inventory a, Inventory b, boolean durability, boolean quantity) {
        return info.somethingodd.OddItem.OddItem.compare(a, b, durability, quantity);
    }

    /**
     * Compares two ItemStack material and durability, ignoring quantity
     *
     * @param a ItemStack to compare
     * @param b ItemStack to compare
     * @return ItemStack are equal
     */
    public static boolean compare(ItemStack a, ItemStack b) {
        return info.somethingodd.OddItem.OddItem.compare(a, b, false);
    }

    /**
     * Compares two ItemStack material, durability, and quantity
     *
     * @param a        ItemStack to compare
     * @param b        ItemStack to compare
     * @param quantity whether to compare quantity
     * @return ItemStack are equal
     */
    public static boolean compare(ItemStack a, ItemStack b, boolean quantity) {
        return info.somethingodd.OddItem.OddItem.compare(a, b, true, quantity);
    }

    /**
     * Compares two ItemStack
     *
     * @param a          ItemStack to compare
     * @param b          ItemStack to compare
     * @param quantity   whether to compare quantity
     * @param durability whether to compare durability
     * @return ItemStack are equal
     */
    public static boolean compare(ItemStack a, ItemStack b, boolean durability, boolean quantity) {
        return info.somethingodd.OddItem.OddItem.compare(a, b, durability, quantity, false);
    }

    /**
     * Compares two ItemStack
     *
     * @param a          ItemStack to compare
     * @param b          ItemStack to compare
     * @param quantity   whether to compare quantity
     * @param durability whether to compare durability
     * @param enchantment whether to compare enchantment
     * @return ItemStack are equal
     */
    public static boolean compare(ItemStack a, ItemStack b, boolean durability, boolean quantity, boolean enchantment) {
        return info.somethingodd.OddItem.OddItem.compare(a, b, durability, quantity, enchantment);
    }




    /**
     * Returns whether player's inventory contains itemStack
     * @param player Player to use inventory
     * @param itemStack ItemStack to look for
     * @return itemStack is contained in inventory
     */
    public static boolean contains(Player player, ItemStack itemStack) {
        return info.somethingodd.OddItem.OddItem.contains(player, itemStack, true);
    }

    /**
     * Returns whether inventory contains itemStack, possibly ignoring durability and quantity
     *
     * @param inventory inventory to look in
     * @param itemStack ItemStack to look for
     * @return itemStack is contained in inventory
     */
    public static boolean contains(Inventory inventory, ItemStack itemStack) {
        return info.somethingodd.OddItem.OddItem.contains(inventory, itemStack, true);
    }

    /**
     * Returns whether player's inventory contains itemStack, possibly ignoring quantity
     * @param player Player to use inventory
     * @param itemStack ItemStack to look for
     * @param quantity whether to check quantity
     * @return itemStack is contained in inventory
     */
    public static boolean contains(Player player, ItemStack itemStack, boolean quantity) {
        return info.somethingodd.OddItem.OddItem.contains(player, itemStack, true, quantity);
    }

    /**
     * Returns whether inventory contains itemStack, possibly ignoring durability and quantity
     * @param inventory inventory to look in
     * @param itemStack ItemStack to look for
     * @param quantity whether to check quantity
     * @return itemStack is contained in inventory
     */
    public static boolean contains(Inventory inventory, ItemStack itemStack, boolean quantity) {
        return info.somethingodd.OddItem.OddItem.contains(inventory, itemStack, true, quantity);
    }

    /**
     * Returns whether player's inventory contains itemStack, possibly ignoring durability and quantity
     * @param player Player to use inventory
     * @param itemStack ItemStack to look for
     * @param durability whether to check durability
     * @param quantity whether to check quantity
     * @return itemStack is contained in inventory
     */
    public static boolean contains(Player player, ItemStack itemStack, boolean durability, boolean quantity) {
        return info.somethingodd.OddItem.OddItem.contains(player.getInventory(), itemStack, durability, quantity);
    }

    /**
     * Returns whether inventory contains itemStack, possibly ignoring durability and quantity
     *
     * @param inventory  inventory to look in
     * @param itemStack  ItemStack to look for
     * @param durability whether to check durability
     * @param quantity   whether to check quantity
     * @return itemStack is contained in inventory
     */
    public static boolean contains(Inventory inventory, ItemStack itemStack, boolean durability, boolean quantity) {
        return info.somethingodd.OddItem.OddItem.contains(inventory, itemStack, durability, quantity, true);
    }

    /**
     * Returns whether inventory contains itemStack, possibly ignoring durability, quantity, and enchantment
     *
     * @param inventory  inventory to look in
     * @param itemStack  ItemStack to look for
     * @param durability whether to check durability
     * @param quantity   whether to check quantity
     * @param enchantment whether to check enchantment
     * @return itemStack is contained in inventory
     */
    public static boolean contains(Inventory inventory, ItemStack itemStack, boolean durability, boolean quantity, boolean enchantment) {
        return info.somethingodd.OddItem.OddItem.contains(inventory, itemStack, durability, quantity, enchantment, true);
    }

    /**
     * Returns whether inventory contains itemStack, possibly ignoring durability, quantity, and enchantment, and allows checking for "at least"
     * @param inventory inventory to look in
     * @param itemStack ItemStack to look for
     * @param durability whether to check durability
     * @param quantity whether to check quantity
     * @param enchantment whether to check enchantment
     * @param exact whether to check exact quantity, or only "at least"
     * @return itemStack is contained in inventory
     */
    public static boolean contains(Inventory inventory, ItemStack itemStack, boolean durability, boolean quantity, boolean enchantment, boolean exact) {
        return info.somethingodd.OddItem.OddItem.contains(inventory, itemStack, durability, quantity, enchantment, exact);
    }

    /**
     * Gets all aliases for the item represented by an ItemStack
     *
     * @param itemStack the ItemStack to use
     * @return List of aliases
     */
    public static Collection<String> getAliases(ItemStack itemStack) {
        return info.somethingodd.OddItem.OddItem.getAliases(itemStack);
    }

    /**
     * Gets all aliases for an item
     *
     * @param query name of item
     * @return <a href="http://download.oracle.com/javase/6/docs/api/java/util/Collection.html?is-external=true">Collection</a>&lt;<a href="http://download.oracle.com/javase/6/docs/api/java/lang/String.html?is-external=true">String</a>&gt; of aliases
     * @throws IllegalArgumentException if no such item exists
     */
    public static Collection<String> getAliases(String query) throws IllegalArgumentException {
        return info.somethingodd.OddItem.OddItem.getAliases(query);
    }

    /**
     * Get an OddItemGroup by name
     *
     * @param name name of group
     * @return group with name
     */
    public static OddItemGroup getItemGroup(String name) {
        return info.somethingodd.OddItem.OddItem.getItemGroup(name);
    }

    /**
     * Get an OddItemGroup by data key
     *
     * @param key top-level key
     * @return matching groups
     */
    public static Collection<OddItemGroup> getItemGroups(String key) {
        return info.somethingodd.OddItem.OddItem.getItemGroups(key);
    }

    /**
     * Get an OddItemGroup by second-level data key
     *
     * @param key top-level key
     * @param key2 second-level key
     * @return matching groups
     */
    public static Collection<OddItemGroup> getItemGroups(String key, String key2) {
        return info.somethingodd.OddItem.OddItem.getItemGroups(key, key2);
    }

    /**
     * Returns an ItemStack of quantity 1 of alias query
     *
     * @param query item name
     * @return <a href="http://jd.bukkit.org/apidocs/org/bukkit/inventory/ItemStack.html?is-external=true">ItemStack</a>
     * @throws IllegalArgumentException exception if item not found, message contains closest match
     */
    public static ItemStack getItemStack(String query) throws IllegalArgumentException {
        return info.somethingodd.OddItem.OddItem.getItemStack(query, 1);
    }

    @Deprecated
    public static ItemStack getItemStack(String query, Integer quantity) throws IllegalArgumentException {
        return info.somethingodd.OddItem.OddItem.getItemStack(query, quantity.intValue());
    }

    /**
     * Returns an ItemStack of specific quantity of alias query
     *
     * @param query item name
     * @param quantity quantity
     * @return <a href="http://jd.bukkit.org/apidocs/org/bukkit/inventory/ItemStack.html?is-external=true">ItemStack</a>
     * @throws IllegalArgumentException exception if item not found, message contains closest match
     */
    public static ItemStack getItemStack(String query, int quantity) throws IllegalArgumentException {
        return info.somethingodd.OddItem.OddItem.getItemStack(query, quantity);
    }

    /**
     * @return <a href="http://download.oracle.com/javase/6/docs/api/java/util/Collection.html?is-external=true">Collection</a>&lt;<a href="http://jd.bukkit.org/apidocs/org/bukkit/inventory/ItemStack.html?is-external=true">ItemStack</a>&gt; of all defined items
     */
    public static Collection<ItemStack> getItemStacks() {
        return items.getAliases().keySet();
    }

    /**
     * Removes itemStack from player's inventory
     *
     * @param player Player to remove itemStack from
     * @param itemStack ItemStack to remove
     * @return amount left over (i.e. player had less than itemStack.getAmount() available)
     */
    public static int removeItem(Player player, ItemStack itemStack) {
        return info.somethingodd.OddItem.OddItem.removeItem(player, itemStack);
    }

    /**
     * Removes itemStacks from players's inventory
     *
     * @param player     Player to remove itemStacks from
     * @param itemStacks ItemStacks to remove
     * @return amounts left over (i.e. player had less than itemStack.getAmount() available)
     */
    public static int[] removeItem(Player player, ItemStack... itemStacks) {
        return info.somethingodd.OddItem.OddItem.removeItem(player, itemStacks);
    }
}
