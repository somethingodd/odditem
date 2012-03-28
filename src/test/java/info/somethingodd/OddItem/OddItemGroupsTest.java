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

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class OddItemGroupsTest {
    private static OddItemBase oddItemBase;
    private static OddItemConfiguration oddItemConfiguration;

    @BeforeClass
    public static void setup() {
        oddItemBase = mock(OddItemBase.class);
        when(oddItemBase.getDataFolder()).thenReturn(new File("src/test/resources"));
        when(oddItemBase.getResource(anyString())).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                String arg0 = (String) invocationOnMock.getArguments()[0];
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(arg0);
                if (inputStream == null) {
                    throw new NullPointerException("Error from getResourceAsStream(" + arg0 + "): ");
                }
                return inputStream;
            }
        });
        when(oddItemBase.getConfig()).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return YamlConfiguration.loadConfiguration(oddItemBase.getResource("config.yml"));
            }
        });
        when(oddItemBase.getDescription()).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                InputStream inputStream = ((OddItemBase) invocationOnMock.getMock()).getResource("plugin.yml");
                return new PluginDescriptionFile(inputStream);
            }
        });
        when(oddItemBase.getLogger()).then(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                class PluginLogger extends Logger {
                    private final StringBuilder prefix;

                    public PluginLogger(Plugin plugin) {
                        super(plugin.getClass().getCanonicalName(), null);
                        prefix = new StringBuilder("[").append((plugin.getDescription().getPrefix() != null ? plugin.getDescription().getPrefix() : plugin.getDescription().getName())).append("]");
                        setUseParentHandlers(false);
                        setLevel(Level.ALL);
                    }

                    @Override
                    public void log(LogRecord logRecord) {
                        logRecord.setMessage(prefix.append(" ").append(logRecord.getMessage()).toString());
                    }
                }
                return new PluginLogger(oddItemBase);
            }
        });
        oddItemConfiguration = new OddItemConfiguration(oddItemBase);
        oddItemConfiguration.configure();
    }

    @AfterClass
    public static void teardown() {
        oddItemBase = null;
        oddItemConfiguration = null;
    }

    @Test
    public void testGroupCount() {
        assertThat(OddItem.groups.groupCount(), equalTo(1));
    }

    @Test
    public void testGroupAliasCount() {
        assertThat(OddItem.groups.aliasCount(), equalTo(2));
    }
}
