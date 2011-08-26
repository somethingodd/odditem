package info.somethingodd.bukkit.OddItem;

import com.google.gson.Gson;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

public class OddItemGroup implements Cloneable {
    private String type = null;
    private List<String> items = null;
    private Iterator<String> iterator = null;

    public OddItemGroup(String type) {
        items = new Vector<String>();
        iterator = items.iterator();
        this.type = type;
    }

    public OddItemGroup(OddItemGroup o) {
        type = o.type;
        items = o.items;
    }

    public static OddItemGroup valueOf(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, OddItemGroup.class);
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() throws NoSuchElementException {
        return iterator.next();
    }
}
