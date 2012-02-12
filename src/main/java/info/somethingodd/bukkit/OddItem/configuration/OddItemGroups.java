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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
            OddItemGroup oddItemGroup = (OddItemGroup) serialized.get(key);
        }
    }

    public OddItemGroups(Map<String, OddItemGroup> groups, boolean b) {
        this.groups = groups;
    }

    public Collection<String> getGroupNames() {
        return Collections.unmodifiableCollection(groups.keySet());
    }

    public Collection<OddItemGroup> getGroups() {
        return Collections.unmodifiableCollection(groups.values());
    }

    public Collection<OddItemGroup> getGroups(String key) {
        List<OddItemGroup> groups = new LinkedList<OddItemGroup>();
        for (OddItemGroup group : this.groups.values())
            if (group.getData().containsKey(key))
                groups.add(group);
        return groups;
    }

    public OddItemGroup getGroup(String name) {
        return groups.get(name);
    }

    public String toString() {
        StringBuilder string = new StringBuilder("OddItemGroups");
        string.append(groups.toString());
        string.append("");
        return string.toString();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
        List<OddItemGroup> groups = new ArrayList<OddItemGroup>();
        for (String s : this.groups.keySet())
            groups.add(this.groups.get(s));
        serialized.put("groups", groups);
        return serialized;
    }

    public OddItemGroups deserialize(Map<String, Object> serialized) {
        return new OddItemGroups(serialized);
    }

    public OddItemGroups valueOf(Map<String, Object> serialized) {
        return new OddItemGroups(serialized);
    }

    public int hashCode() {
        return groups.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof OddItemGroups)) return false;
        if (this == other) return true;
        if (groups.equals(((OddItemGroups) other).groups)) return true;
        return false;
    }
}
