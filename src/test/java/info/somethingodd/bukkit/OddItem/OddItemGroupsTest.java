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

import info.somethingodd.bukkit.OddItem.configuration.OddItemGroup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import static org.mockito.Mockito.when;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class OddItemGroupsTest {
    @Mock
    private OddItemBase oddItemBase;
    @Mock
    private OddItemConfiguration oddItemConfiguration;

    @Before
    public void setup() {
        when(oddItemBase.getDataFolder()).thenReturn(new File("src/main/resources/"));
        oddItemConfiguration.configure();
    }

    @Test
    public void testReproducible() {
        OddItemGroup oddItemGroup = new OddItemGroup(new ItemStack(Material.GOLD_INGOT, 32), new ItemStack(Material.DIAMOND_AXE, 1));
        Assert.assertThat(oddItemGroup.serialize(), CoreMatchers.equalTo(OddItemGroup.valueOf(oddItemGroup.serialize()).serialize()));
    }

}
