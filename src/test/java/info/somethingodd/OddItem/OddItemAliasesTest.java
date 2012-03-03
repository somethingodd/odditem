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
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class OddItemAliasesTest {
    private static OddItemBase oddItemBase;
    private static OddItemConfiguration oddItemConfiguration;

    @BeforeClass
    public static void setup() {
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
                if (res == null) {
                    throw new NullPointerException("Error from getResourceAsStream(" + arg0 + "): ");
                }
                return res;
            }
        });
        oddItemBase.log = Logger.getLogger("OddItemAliasesTest");
        oddItemConfiguration = mock(OddItemConfiguration.class);
        oddItemConfiguration = new OddItemConfiguration(oddItemBase);
        oddItemConfiguration.configure();
    }

    @AfterClass
    public static void teardown() {
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

    @Test
    public void testAliases() {
        assertThat(OddItem.getAliases(new ItemStack(Material.STONE, 1)), equalTo(OddItem.getAliases("rock")));
        assertThat(OddItem.getAliases("rock"), equalTo(OddItem.getAliases("stone")));
    }

    @Test
    public void testLowercase() {
        assertThat(OddItem.getItemStack("RoCk"), equalTo(OddItem.getItemStack("sToNe")));
    }

    @Test
    public void serialize() {
        Map<String, Object> serialTestMap = new TreeMap<String, Object>(OddItem.ALPHANUM_COMPARATOR);
        serialTestMap.put("0;0", Arrays.asList("air", "nothing"));
        serialTestMap.put("1;0", Arrays.asList("rock", "stone"));
        serialTestMap.put("2;0", Arrays.asList("grass"));
        serialTestMap.put("3;0", Arrays.asList("dirt", "soil"));
        serialTestMap.put("4;0", Arrays.asList("cobble", "cobblestone"));
        serialTestMap.put("5;0", Arrays.asList("plank", "wood"));
        serialTestMap.put("6;0", Arrays.asList("sapling", "oaksapling"));
        serialTestMap.put("6;1", Arrays.asList("pinesapling"));
        serialTestMap.put("6;2", Arrays.asList("birchsapling"));
        serialTestMap.put("7;0", Arrays.asList("adminium", "bedrock"));
        serialTestMap.put("8;0", Arrays.asList("water"));
        serialTestMap.put("9;0", Arrays.asList("stillwater", "swater"));
        serialTestMap.put("10;0", Arrays.asList("lava"));
        serialTestMap.put("11;0", Arrays.asList("stilllava", "slava"));
        serialTestMap.put("12;0", Arrays.asList("sand"));
        serialTestMap.put("13;0", Arrays.asList("gravel"));
        serialTestMap.put("14;0", Arrays.asList("goldore", "gore"));
        serialTestMap.put("15;0", Arrays.asList("iore", "ironore"));
        serialTestMap.put("16;0", Arrays.asList("coalore", "core"));

        Map<ItemStack, List<String>> itemTestMap = new TreeMap<ItemStack, List<String>>(OddItem.ITEM_STACK_COMPARATOR);
        itemTestMap.put(new ItemStack(0, 1, (short) 0), Arrays.asList("air", "nothing"));
        itemTestMap.put(new ItemStack(1, 1, (short) 0), Arrays.asList("rock", "stone"));
        itemTestMap.put(new ItemStack(2, 1, (short) 0), Arrays.asList("grass"));
        itemTestMap.put(new ItemStack(3, 1, (short) 0), Arrays.asList("dirt", "soil"));
        itemTestMap.put(new ItemStack(4, 1, (short) 0), Arrays.asList("cobble", "cobblestone"));
        itemTestMap.put(new ItemStack(5, 1, (short) 0), Arrays.asList("plank", "wood"));
        itemTestMap.put(new ItemStack(6, 1, (short) 0), Arrays.asList("oaksapling", "sapling"));
        itemTestMap.put(new ItemStack(6, 1, (short) 1), Arrays.asList("pinesapling"));
        itemTestMap.put(new ItemStack(6, 1, (short) 2), Arrays.asList("birchsapling"));
        itemTestMap.put(new ItemStack(7, 1, (short) 0), Arrays.asList("adminium", "bedrock"));
        itemTestMap.put(new ItemStack(8, 1, (short) 0), Arrays.asList("water"));
        itemTestMap.put(new ItemStack(9, 1, (short) 0), Arrays.asList("stillwater", "swater"));
        itemTestMap.put(new ItemStack(10, 1, (short) 0), Arrays.asList("lava"));
        itemTestMap.put(new ItemStack(11, 1, (short) 0), Arrays.asList("slava", "stilllava"));
        itemTestMap.put(new ItemStack(12, 1, (short) 0), Arrays.asList("sand"));
        itemTestMap.put(new ItemStack(13, 1, (short) 0), Arrays.asList("gravel"));
        itemTestMap.put(new ItemStack(14, 1, (short) 0), Arrays.asList("goldore", "gore"));
        itemTestMap.put(new ItemStack(15, 1, (short) 0), Arrays.asList("iore", "ironore"));
        itemTestMap.put(new ItemStack(16, 1, (short) 0), Arrays.asList("coalore", "core"));

        for (ItemStack itemStack : itemTestMap.keySet()) {
            List<String> itemList = new LinkedList<String>();
            itemList.addAll(OddItemAliases.valueOf(serialTestMap).getAliases(itemStack));
            assertThat(itemList, equalTo(itemTestMap.get(itemStack)));
        }
    }
}