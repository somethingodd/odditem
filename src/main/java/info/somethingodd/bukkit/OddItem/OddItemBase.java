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

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemBase extends JavaPlugin {
    protected Logger log = null;
    protected String logPrefix = null;
    protected String configFile = null;

    @Override
    public void onDisable() {
        log.info(logPrefix + "disabled");
        OddItem.clear();
        logPrefix = null;
        log = null;
        configFile = null;
    }

    @Override
    public void onEnable() {
        log = Bukkit.getServer().getLogger();
        logPrefix = "[" + getDescription().getName() + "] ";
        log.info(logPrefix + getDescription().getVersion() + " enabled");
        configFile = getDataFolder() + System.getProperty("file.separator") + "OddItem.yml";
        try {
            OddItemConfiguration configuration = new OddItemConfiguration(this);
            configuration.configure(configFile);
        } catch (Exception e) {
            log.severe(logPrefix + "Configuration error!");
            e.printStackTrace();
        }
        getCommand("odditem").setExecutor(new OddItemCommandExecutor(this));
        log.info(logPrefix + OddItem.itemMap.size() + " aliases loaded.");
    }

    private void doUpdate() {
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;
        String line;
        URL url;
        URLConnection urlConnection;
        try {
            url = new URL("http://odditem.bukkit.somethingodd.info/OddItem.yml");
            try {
                urlConnection = url.openConnection();
                urlConnection.setDoOutput(true);
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                    try {
                        bufferedWriter = new BufferedWriter(new FileWriter(new File(getDataFolder() + System.getProperty("file.separator") + "OddItem.yml.new")));
                        try {
                            while ((line = bufferedReader.readLine()) != null) {
                                bufferedWriter.write(line);
                            }
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        } catch (IOException e) {
                            log.warning(logPrefix + "Failed to write to update file...");
                        }
                    } catch (IOException e) {
                        log.warning(logPrefix + "Failed to open update file...");
                    }
                } catch (IOException e) {
                    log.warning(logPrefix + "Failed to open update stream...");
                }
            } catch (IOException e) {
                log.warning(logPrefix + "Failed to connect to update...");
            }
        } catch (MalformedURLException e) {
            log.severe(logPrefix + "Failed to construct update URL...");
        }
    }
}