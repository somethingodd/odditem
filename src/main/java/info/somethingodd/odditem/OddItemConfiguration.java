/* This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, "either" version 3 of the License, "or"
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, "see" <http://www.gnu.org/licenses/>.
 */
package info.somethingodd.odditem;

import info.somethingodd.odditem.goddamnithidendraputitinitsownplugin.Metrics;
import info.somethingodd.odditem.configuration.OddItemAliases;
import info.somethingodd.odditem.configuration.OddItemGroup;
import info.somethingodd.odditem.configuration.OddItemGroups;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemConfiguration {
    private static String comparator;
    private static int maxBlockId;
    private final OddItemBase oddItemBase;

    /**
     * Constructor
     * @param oddItemBase Base plugin
     */
    public OddItemConfiguration(OddItemBase oddItemBase) {
        this.oddItemBase = oddItemBase;
    }

    /**
     * Gets comparator type
     * @return comparator string
     */
    public static String getComparator() {
        return comparator;
    }

    /**
     * Gets maximum block ID, which might differ between servers in future
     * @return maximum block ID
     */
    public static int getMaxBlockId() {
        return maxBlockId;
    }

    /**
     * Configures OddItem
     */
    public void configure() {
        try {
            initialConfig("config.yml");
            initialConfig("groups.yml");
            initialConfig("items.yml");
        } catch (Exception e) {
            oddItemBase.getLogger().warning("Exception writing initial configuration files: " + e.getMessage());
            e.printStackTrace();
        }
        YamlConfiguration yamlConfiguration = (YamlConfiguration) oddItemBase.getConfig();
        comparator = yamlConfiguration.getString("comparator", "r");
        maxBlockId = yamlConfiguration.getInt("maxBlockId", 256);

        if (yamlConfiguration.getBoolean("metrics", true)) {
            try {
                new Metrics(oddItemBase).start();
            } catch (IOException e) {
                oddItemBase.getLogger().warning("Failed to run Metrics. This is not an error.");
            }
        }

        ConfigurationSerialization.registerClass(OddItemAliases.class);

        ConfigurationSerialization.registerClass(OddItemAliases.class);
        YamlConfiguration itemConfiguration = new YamlConfiguration();
        try {
            itemConfiguration.load(new File(oddItemBase.getDataFolder(), "items.yml"));
        } catch (Exception e) {
            oddItemBase.getLogger().warning("Error opening items.yml!");
        }
        YamlConfiguration itemConfigurationDefault = new YamlConfiguration();
        try {
            itemConfigurationDefault.load(oddItemBase.getResource("items.yml"));
            itemConfiguration.setDefaults(itemConfigurationDefault);
        } catch (Exception e) {
            oddItemBase.getLogger().warning("Error opening default resource for items.yml!");
        }
        OddItem.items = OddItemAliases.valueOf(itemConfiguration.getConfigurationSection("items").getValues(false));

        ConfigurationSerialization.registerClass(OddItemGroup.class);
        ConfigurationSerialization.registerClass(OddItemGroups.class);
        YamlConfiguration groupConfiguration = new YamlConfiguration();
        try {
            groupConfiguration.load(new File(oddItemBase.getDataFolder(), "groups.yml"));
        } catch (Exception e) {
            oddItemBase.getLogger().warning("Error opening groups.yml!");
        }
        OddItem.groups = OddItemGroups.valueOf(groupConfiguration.getConfigurationSection("groups").getValues(false));
    }

    /**
     * Copies file from internal JAR resource to disk
     * @param filename filename to get from resources
     * @throws IOException Exception thrown upon error copying file
     */
    private void initialConfig(String filename) throws IOException {
        File file = new File(oddItemBase.getDataFolder(), filename);
        if (!file.exists()) {
            BufferedReader src = null;
            BufferedWriter dst = null;
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                src = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + filename)));
                dst = new BufferedWriter(new FileWriter(file));
                String line = src.readLine();
                while (line != null) {
                    dst.write(line + "\n");
                    line = src.readLine();
                }
                src.close();
                dst.close();
                oddItemBase.getLogger().info("Wrote default " + filename);
            } catch (IOException e) {
                oddItemBase.getLogger().warning("Error writing default " + filename);
            } finally {
                try {
                    src.close();
                    dst.close();
                } catch (Exception e) {
                    oddItemBase.getLogger().severe("Your system is whack.");
                }
            }
        }
    }
}
