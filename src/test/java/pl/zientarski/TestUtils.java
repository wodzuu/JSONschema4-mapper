package pl.zientarski;

import org.json.JSONArray;

import java.util.HashSet;
import java.util.Set;

public final class TestUtils {
    public static Set<String> toStringSet(final JSONArray jsonArray) {
        final Set<String> result = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.getString(i));
        }
        return result;
    }

}
