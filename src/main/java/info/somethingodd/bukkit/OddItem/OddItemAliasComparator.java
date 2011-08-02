package info.somethingodd.bukkit.OddItem;

import java.io.Serializable;
import java.util.Comparator;

public class OddItemAliasComparator implements Comparator, Serializable {

    @Override
    public int compare(Object o1, Object o2) {
        String o11 = (String) o1;
        String o21 = (String) o2;
        Integer l = o11.length();
        if (o21.length() < l) l = o21.length();
        for (int i = 0; i < l; i++) {
            Character c1 = Character.toLowerCase(o11.charAt(i));
            Character c2 = Character.toLowerCase(o21.charAt(i));
            if (c1 > c2) return 1;
            if (c1 < c2) return -1;
        }
        if (o11.length() < o21.length())
            return -1;
        if (o11.length() > o21.length())
            return 1;
        return 0;

    }

}
