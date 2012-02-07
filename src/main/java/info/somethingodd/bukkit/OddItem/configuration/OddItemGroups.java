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
package info.somethingodd.bukkit.OddItem.configuration;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class OddItemGroups implements ConfigurationSerializable {
    private final Map<String, OddItemGroup> groups;

    public OddItemGroups(Map<String, Object> serialized) {
        groups = new TreeMap<String, OddItemGroup>(String.CASE_INSENSITIVE_ORDER);
        for (String key : serialized.keySet()) {
            Object oddItemGroup = serialized.get(key);
        }
    }

    public Collection<String> getGroupNames() {
        return Collections.unmodifiableCollection(groups.keySet());
    }

    public Collection<OddItemGroup> getGroups() {
        return Collections.unmodifiableCollection(groups.values());
    }

    public OddItemGroup getGroup(String group) {
        return groups.get(group);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        for (String s : groups.keySet())
            serialized.put(groups.get(s).getName(), groups.get(s));
        return serialized;
    }

    public OddItemGroups deserialize(Map<String, Object> serialized) {
        return new OddItemGroups(serialized);
    }

    public OddItemGroups valueOf(Map<String, Object> serialized) {
        return new OddItemGroups(serialized);
    }
}
