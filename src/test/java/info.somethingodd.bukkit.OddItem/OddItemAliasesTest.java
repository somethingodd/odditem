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
import org.bukkit.inventory.ItemStack;
import org.hamcrest.CoreMatchers;
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
    private ItemStack dirt;
    private ItemStack stone;
    private Map<String, ItemStack> items;
    private Set<String> namesDirt;
    private Set<String> namesStone;
    private OddItemAliases oddItemAliases;
    private Map<String, Object> serialized;

    @Before
    public void setup() {
        dirt = new ItemStack(3, 1, (short) 0);
        stone = new ItemStack(1, 1, (short) 0);
        namesDirt = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        namesDirt.add("dirt");
        namesStone = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        namesStone.add("stone");
        namesStone.add("rock");

        serialized = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        serialized.put("3;0", namesDirt);
        serialized.put("1;0", namesStone);
        oddItemAliases = new OddItemAliases(serialized);

        items = new TreeMap<String, ItemStack>(String.CASE_INSENSITIVE_ORDER);
        items.put("dirt", dirt);
        items.put("rock", stone);
        items.put("stone", stone);

        aliases = new TreeMap<String, Set<String>>(String.CASE_INSENSITIVE_ORDER);
        aliases.put("dirt", namesDirt);
        aliases.put("stone", namesStone);
        aliases.put("rock", namesStone);
    }

    @After
    public void teardown() {
        aliases = null;
        dirt = null;
        stone = null;
        items = null;
        namesDirt = null;
        namesStone = null;
        oddItemAliases = null;
        serialized = null;
    }

    @Test
    public void serialize() {
        Object c = new Object();
        Map<String, Object> a = oddItemAliases.serialize();

        Assert.assertThat("Serialization reproducibility", a, CoreMatchers.equalTo(oddItemAliases.serialize()));
        Assert.assertTrue(a.equals(oddItemAliases.serialize()));
        Assert.assertTrue(a.equals(OddItemAliases.valueOf(serialized).serialize()));
    }

    @Test
    public void deserialize() {
        OddItemAliases a = OddItemAliases.deserialize(serialized);
        Assert.assertTrue(a.equals(oddItemAliases));
    }

    @Test
    public void checkAliases() {
        Assert.assertTrue(oddItemAliases.getAliases().equals(aliases));
    }

    @Test
    public void checkItems() {
        Assert.assertTrue(oddItemAliases.getItems().equals(items));
    }
}