package main.java.treebreaker.plugin.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
    @author Daniel Allen
    25-Nov-2019
 */
public class Utils {

    private static final Map<String, Object> data = new HashMap<>();

    public static void setProperty(String key, Object value) {
        data.put(key, value);
    }

    public static Object getProperty(String key) {
        return data.get(key);
    }

    public static <T> T getProperty(String key, Class<T> clazz) {
        return clazz.cast(data.get(key));
    }

    public static <T> T getProperty(String key, T def) {
        if (data.containsKey(key)) {
            Object val = data.get(key);
            if (def.getClass().isAssignableFrom(val.getClass())) {
                return (T) val;
            }
        }
        return def;
    }

    public static <T> T getProperty(String key, T def, Class<T> clazz) {
        if (data.containsKey(key)) {
            return clazz.cast(data.get(key));
        }
        return clazz.cast(def);
    }

    public static boolean contains(Object[] list, Object item) {
        if (item == null) {
            for (int i = 0; i < list.length; i++) {
                if (list[i] == null) {
                    return true;
                }
            }
        }
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static String getAllProperties() {
        return joinList(new ArrayList(data.keySet()));
    }

    public static Set getAllPropertyKeys() {
        return data.keySet();
    }

    public static String joinList(List<String> list) {
        //could use String.join or Stream but I found this approach to consistantly be 150-500ns faster
        StringBuilder sb = new StringBuilder(list.size());
        for (int z = 0; z < list.size() - 1; z++) {
            sb.append(list.get(z));
            sb.append(',').append(' ');
        }
        sb.append(list.get(list.size() - 1));
        return sb.toString();
    }

}
