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

import info.somethingodd.bukkit.OddItem.configuration.OddItemAliases;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemAliasesTest {
    private Map<String, Set<String>> aliases;
    private Map<String, Set<String>> aliases2;
    private ItemStack dirt;
    private ItemStack stone;
    private Map<String, ItemStack> items;
    private Map<String, ItemStack> items2;
    private Set<String> names;
    private Set<String> names2;
    private OddItemAliases oddItemAliases;
    private OddItemAliases oddItemAliases2;
    private Map<String, Object> serialized;
    private Map<String, Object> serialized2;

    @Before
    public void setup() {

        dirt = new ItemStack(3, 1, (short) 0);
        names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        names.add("dirt");
        serialized = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        serialized.put("3;0", names);
        oddItemAliases = new OddItemAliases(serialized);
        items = new TreeMap<String, ItemStack>(String.CASE_INSENSITIVE_ORDER);
        items.put("dirt", dirt);
        aliases = new TreeMap<String, Set<String>>(String.CASE_INSENSITIVE_ORDER);
        aliases.put("dirt", names);


        stone = new ItemStack(1, 1, (short) 0);
        names2 = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        names2.add("stone");
        names2.add("rock");
        serialized2 = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        serialized2.put("1;0", names2);
        oddItemAliases2 = new OddItemAliases(serialized2);
        items2 = new TreeMap<String, ItemStack>(String.CASE_INSENSITIVE_ORDER);
        items2.put("stone", stone);
        items2.put("rock", stone);
        aliases2 = new TreeMap<String, Set<String>>(String.CASE_INSENSITIVE_ORDER);
        aliases2.put("stone", names2);
        aliases2.put("rock", names2);
    }

    @After
    public void teardown() {
        aliases = null;
        aliases2 = null;
        dirt = null;
        stone = null;
        items = null;
        items2 = null;
        names = null;
        names2 = null;
        oddItemAliases = null;
        oddItemAliases2 = null;
        serialized = null;
        serialized2 = null;
    }

    @Test
    public void serialize() {
        Object c = new Object();
        Map<String, Object> a = oddItemAliases.serialize();
        Map<String, Object> b = oddItemAliases2.serialize();

        Assert.assertTrue(a.equals(oddItemAliases.serialize()));
        Assert.assertTrue(b.equals(oddItemAliases2.serialize()));
        Assert.assertTrue(a.equals(OddItemAliases.valueOf(serialized).serialize()));
        Assert.assertTrue(b.equals(OddItemAliases.valueOf(serialized2).serialize()));
    }

    @Test
    public void deserialize() {
        YamlConfiguration y = new YamlConfiguration();
        y.createSection("items");
        ConfigurationSection yitems = y.getConfigurationSection("items");
        TreeSet<String> yitemsset = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        yitemsset.add("rock");
        yitemsset.add("stone");
        yitems.set("1;0", yitemsset);

        Assert.assertTrue(OddItemAliases.valueOf(yitems.getValues(false)).equals(oddItemAliases2));

        OddItemAliases a = OddItemAliases.deserialize(serialized);
        OddItemAliases b = OddItemAliases.deserialize(serialized2);
        Assert.assertTrue(a.equals(oddItemAliases));
        Assert.assertTrue(b.equals(oddItemAliases2));
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void checkAliases() {
        Assert.assertTrue(oddItemAliases.getAliases().equals(aliases));
        Assert.assertTrue(oddItemAliases2.getAliases().equals(aliases2));
    }

    @Test
    public void checkItems() {
        Assert.assertTrue(oddItemAliases.getItems().equals(items));
    }

    @Test
    public void notEqual() {
        Assert.assertFalse(oddItemAliases.equals(oddItemAliases2));
        Assert.assertFalse(oddItemAliases.getAliases().equals(oddItemAliases2.getAliases()));
        Assert.assertFalse(oddItemAliases.getItems().equals(oddItemAliases2.getItems()));
    }
}