package info.somethingodd.bukkit.OddItem;

import org.bukkit.Material;

import java.io.Serializable;
import java.util.Comparator;

public class OddItemIDComparator implements Comparator, Serializable {

    @Override
    public int compare(Object o1, Object o2) {
        if (!(o1 instanceof String && o2 instanceof String))
            return 0;
        String o11 = (String) o1;
        String o21 = (String) o2;
        String o12 = null;
        String o22 = null;
        if (o11.contains(";")) {
            o12 = o11.split(";")[1];
            o11 = o11.split(";")[0];
        }
        if (o12.contains(";")) {
            o22 = o21.split(";")[1];
            o21 = o21.split(";")[0];
        }
        Integer oi11 = 0;
        Integer oi21 = 0;
        Integer oi12 = 0;
        Integer oi22 = 0;
        try {
            oi11 = Integer.parseInt(o11);
        } catch (NumberFormatException e) {
            oi11 = Material.getMaterial(o11).getId();
        }
        try {
            oi12 = Integer.parseInt(o12);
        } catch (NumberFormatException e) {
        }
        try {
            oi21 = Integer.parseInt(o21);
        } catch (NumberFormatException e) {
            oi21 = Material.getMaterial(o21).getId();
        }
        try {
            oi22 = Integer.parseInt(o22);
        } catch (NumberFormatException e) {
        }
        if (oi11 == oi21) {
            if (oi12 > oi22) return 1;
            if (oi12 < oi22) return -1;
            return 0;
        }
        if (oi11 > oi21) return 1;
        if (oi11 < oi21) return -1;
        return 0;
    }

}
