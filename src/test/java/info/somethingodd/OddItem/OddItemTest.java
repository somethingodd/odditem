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
package info.somethingodd.OddItem;

import info.somethingodd.OddItem.configuration.OddItemAliases;
import info.somethingodd.OddItem.configuration.OddItemGroups;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class OddItemTest {
    private YamlConfiguration itemsConfiguration;
    private YamlConfiguration groupsConfiguration;

    @Before
    public void setup() {
        itemsConfiguration = YamlConfiguration.loadConfiguration(getClass().getResourceAsStream("/items.yml"));
        groupsConfiguration = YamlConfiguration.loadConfiguration(getClass().getResourceAsStream("/groups.yml"));
    }

    @After
    public void teardown() {
        itemsConfiguration = null;
        groupsConfiguration = null;
    }

    @Test
    public void testReproducible() {
        OddItem.items = OddItemAliases.valueOf(itemsConfiguration.getConfigurationSection("items").getValues(false));
        assertThat(OddItem.items, instanceOf(OddItemAliases.class));

        OddItem.groups = OddItemGroups.valueOf(groupsConfiguration.getConfigurationSection("groups").getValues(false));
        assertThat(OddItem.groups, instanceOf(OddItemGroups.class));
    }

    @Test
    public void testAliasesReversible() {
        Map<String, Object> serialTestMap = new TreeMap<String, Object>(OddItem.ALPHANUM_COMPARATOR);
        serialTestMap.put("0;0", Arrays.asList("air", "nothing"));
        serialTestMap.put("1;0", Arrays.asList("rock", "stone"));
        Map<ItemStack, Collection<String>> itemTestMap = new TreeMap<ItemStack, Collection<String>>(OddItem.ITEM_STACK_COMPARATOR);
        itemTestMap.put(new ItemStack(Material.AIR, 1, (short) 0), Arrays.asList("air", "nothing"));
        itemTestMap.put(new ItemStack(Material.STONE, 1, (short) 0), Arrays.asList("rock", "stone"));

        OddItemAliases aliasesTest = new OddItemAliases(itemTestMap, false);

        assertThat(aliasesTest.serialize(), equalTo(serialTestMap));
        assertThat(OddItemAliases.valueOf(serialTestMap), equalTo(aliasesTest));
    }
}