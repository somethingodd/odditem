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
package info.somethingodd.odditem.configuration;

import info.somethingodd.odditem.OddItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Gordon Pettey (petteyg359@gmail.com)
 */
public class Groups implements ConfigurationSerializable {
    private final Map<String, Group> groups;
    private final Map<Group, Set<String>> aliases;

    public Groups(Map<String, Object> serialized) {
        aliases = new HashMap<Group, Set<String>>();
        groups = new TreeMap<String, Group>(OddItem.ALPHANUM_COMPARATOR);
        for (String key : serialized.keySet()) {
            Group group = Group.valueOf(((ConfigurationSection) serialized.get(key)).getValues(false));
            if (aliases.get(group) == null)
                aliases.put(group, new TreeSet<String>(OddItem.ALPHANUM_COMPARATOR));
            aliases.get(group).addAll(((ConfigurationSection) serialized.get(key)).getStringList("aliases"));
            aliases.get(group).add(key);
            for (String alias : aliases.get(group)) {
                groups.put(alias, group);
            }
        }
    }

    /**
     * @return number of groups loaded
     */
    public int groupCount() {
        return aliases.size();
    }

    /**
     * @return number of aliases loaded
     */
    public int aliasCount() {
        return groups.size();
    }

    /**
     * Gets an Group by alias
     * @param alias group alias to retrieve
     * @return Group
     */
    public Group getGroup(String alias) {
        return groups.get(alias);
    }

    /**
     * Checks for groups containing data key
     * @param key data key to check
     * @return Collection of groups containing key
     */
    public Collection<Group> getGroups(String key) {
        Collection<Group> groups = new HashSet<Group>();
        for (Group group : this.groups.values())
            if (group.match(key))
                groups.add(group);
        return groups;
    }

    /**
     * Checks for groups containing second-level data key
     * @param key top-level key to check
     * @param key2 second-level key to check
     * @return Collection of groups containing keys
     */
    public Collection<Group> getGroups(String key, String key2) {
        Collection<Group> groups = new HashSet<Group>();
        for (Group group : this.groups.values())
            if (group.match(key, key2))
                groups.add(group);
        return groups;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new TreeMap<String, Object>(OddItem.ALPHANUM_COMPARATOR);
        return serialized;
    }

    public static Groups deserialize(Map<String, Object> serialized) {
        return new Groups(serialized);
    }

    public static Groups valueOf(Map<String, Object> serialized) {
        return new Groups(serialized);
    }

    public int hashCode() {
        return groups.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Groups)) return false;
        if (this == other) return true;
        if (groups.equals(((Groups) other).groups)) return true;
        return false;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Groups");
        List<Group> groups = new LinkedList<Group>(this.groups.values());
        for (int i = 0; i < groups.size(); i++) {
            str.append(groups.get(i).toString());
            if (i < groups.size() - 1) {
                str.append(", ");
            }
        }
        str.append("}");
        return str.toString();
    }
}
