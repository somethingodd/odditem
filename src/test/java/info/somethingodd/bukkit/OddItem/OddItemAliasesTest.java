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
import info.somethingodd.bukkit.OddItem.util.AlphanumComparator;
import info.somethingodd.bukkit.OddItem.util.ItemStackComparator;
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
    private Map<ItemStack, Set<String>> aliases;
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
        namesDirt = new TreeSet<String>(new AlphanumComparator());
        namesDirt.add("dirt");
        namesStone = new TreeSet<String>(new AlphanumComparator());
        namesStone.add("stone");
        namesStone.add("rock");

        serialized = new TreeMap<String, Object>(new AlphanumComparator());
        serialized.put("3;0", namesDirt);
        serialized.put("1;0", namesStone);
        oddItemAliases = new OddItemAliases(serialized);

        items = new TreeMap<String, ItemStack>(new AlphanumComparator());
        items.put("dirt", dirt);
        items.put("rock", stone);
        items.put("stone", stone);

        aliases = new TreeMap<ItemStack, Set<String>>(new ItemStackComparator());
        aliases.put(dirt, namesDirt);
        aliases.put(stone, namesStone);
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
        Map<String, Object> a = oddItemAliases.serialize();
        Assert.assertThat(a, CoreMatchers.equalTo(oddItemAliases.serialize()));
        Assert.assertThat(a, CoreMatchers.equalTo(OddItemAliases.valueOf(serialized).serialize()));
    }

    @Test
    public void deserialize() {
        OddItemAliases a = OddItemAliases.deserialize(serialized);
        Assert.assertThat(a, CoreMatchers.equalTo(oddItemAliases));
    }

    @Test
    public void checkAliases() {
        Assert.assertThat(oddItemAliases.getAliases().toString(), CoreMatchers.equalTo(aliases.toString()));
    }

    @Test
    public void checkItems() {
        Assert.assertThat(oddItemAliases.getItems(), CoreMatchers.equalTo(items));
    }
}