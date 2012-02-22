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

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class OddItemTest {
    private OddItemBase oddItemBase;
    private OddItemConfiguration oddItemConfiguration;
    private Map<String, Object> serialTestMap;
    private Map<ItemStack, List<String>> itemTestMap;

    @Before
    public void setup() {
        serialTestMap = new TreeMap<String, Object>(OddItem.ALPHANUM_COMPARATOR);
        serialTestMap.put("0;0", Arrays.asList("air", "nothing"));
        serialTestMap.put("1;0", Arrays.asList("rock", "stone"));
        serialTestMap.put("2;0", Arrays.asList("grass"));
        serialTestMap.put("3;0", Arrays.asList("dirt", "soil"));
        serialTestMap.put("4;0", Arrays.asList("cobble", "cobblestone"));

        itemTestMap = new TreeMap<ItemStack, List<String>>(OddItem.ITEM_STACK_COMPARATOR);
        itemTestMap.put(new ItemStack(Material.AIR, 1, (short) 0), Arrays.asList("air", "nothing"));
        itemTestMap.put(new ItemStack(Material.STONE, 1, (short) 0), Arrays.asList("rock", "stone"));
        itemTestMap.put(new ItemStack(Material.STONE, 1, (short) 0), Arrays.asList("grass"));
        itemTestMap.put(new ItemStack(Material.STONE, 1, (short) 0), Arrays.asList("dirt", "soil"));
        itemTestMap.put(new ItemStack(Material.STONE, 1, (short) 0), Arrays.asList("cobble", "cobblestone"));

        oddItemBase = mock(OddItemBase.class);
        when(oddItemBase.getDataFolder()).thenReturn(new File("src/main/resources"));
        FileConfiguration fc = YamlConfiguration.loadConfiguration(new File(oddItemBase.getDataFolder(), "config.yml"));
        when(oddItemBase.getConfig()).thenReturn(fc);
        when(oddItemBase.getResource(anyString())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                OddItemBase oddItemBase1 = ((OddItemBase) invocationOnMock.getMock());
                String arg0 = (String) invocationOnMock.getArguments()[0];
                InputStream res = getClass().getResourceAsStream(arg0);
                if (res == null)
                    throw new NullPointerException("Error from getResourceAsStream(" + arg0 + "): ");
                return res;
            }
        });
        oddItemBase.log = Logger.getLogger("OddItemTest");
        oddItemConfiguration = mock(OddItemConfiguration.class);
        oddItemConfiguration = new OddItemConfiguration(oddItemBase);
        oddItemConfiguration.configure();
    }

    @After
    public void teardown() {
        serialTestMap = null;
        itemTestMap = null;
        oddItemConfiguration = null;
        oddItemBase = null;
    }

    @Test
    public void testItemCount() {
        assertTrue(OddItem.items.itemCount() == 364);
    }

    @Test
    public void testItemAliasCount() {
        assertTrue(OddItem.items.aliasCount() == 801);
    }

    @Test
    public void testGroupCount() {
        assertTrue(OddItem.groups.groupCount() == 1);
    }

    @Test
    public void testGroupAliasCount() {
        assertTrue(OddItem.groups.aliasCount() == 2);
    }
}