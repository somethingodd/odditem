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

import info.somethingodd.bukkit.OddItem.configuration.OddItemStack;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemStackTest {
    private OddItemStack oddItemStack;
    private Map<String, Object> serialized;

    @Before
    public void setup() {
        serialized = new TreeMap<String, Object>();
        List<String> names = new ArrayList<String>();
        names.add("dirt");
        serialized.put("3;0", names);
        oddItemStack = new OddItemStack(names, 3, (short) 0);
    }

    @After
    public void teardown() {
        oddItemStack = null;
        serialized = null;
    }

    @Test
    public void serialize() {
        Assert.assertThat(oddItemStack.serialize(), CoreMatchers.is(serialized));
    }

    @Test
    public void deserialize() {
        Assert.assertTrue(OddItemStack.deserialize(serialized).equals(oddItemStack));
    }
}
