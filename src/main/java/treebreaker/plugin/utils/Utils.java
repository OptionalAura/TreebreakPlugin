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

    /**
     * Gets a string between two substrings of a larger string.
     * <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * <b>Note:</b> {@code end} will only be searched for from the index of
     * {@code start + start.length()}
     *
     * @param input Original string to search
     * @param start The marker designating the start of the query
     * @param end The marker designating the end of the query
     * @return The string between {@code start} and {@code end}, or all
     * characters after {@code start}
     * @throws StringIndexOutOfBoundsException if
     * <ol>
     * <li>{@code input.indexOf(start)} &lt; 0</li>
     * <li>{@code input.indexOf(start)} &gt;= {@code input.length}</li>
     * <li>{@code input.indexOf(start)} &gt; {@code input.indexOf(end)}</li>
     * </ol>
     */
    public static String stringBetween(String input, String start, String end) {
        if (input == null || input.length() == 0) {
            return input;
        }

        int startLength = start.length();

        if (startLength >= input.length()) {
            return "";
        }

        int startIndex = input.indexOf(start) + startLength;
        int endIndex = input.indexOf(end, startIndex);

        if (startIndex == -1 || startIndex >= input.length()) {
            throw new StringIndexOutOfBoundsException("Start index out of bounds");
        } else if (endIndex == -1) {
            return input.substring(startIndex);
        } else if (endIndex < startIndex) {
            throw new StringIndexOutOfBoundsException("End index (" + endIndex + ") cannot be less than or equal to start index (" + startIndex + ")");
        }

        return input.substring(startIndex, endIndex);
    }

    /**
     * Gets a string between two substrings of a larger string.
     * <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * <b>Note:</b> {@code end} will only be searched for from the index of
     * {@code start + start.length()}
     *
     * @param input Original string to search
     * @param start The marker designating the start of the query
     * @return The string between {@code start} and {@code end}, or all
     * characters after {@code start}
     * @throws StringIndexOutOfBoundsException if
     * <ol>
     * <li>{@code input.indexOf(start)} &lt; 0</li>
     * <li>{@code input.indexOf(start)} &gt;= {@code input.length}</li>
     * <li>{@code input.indexOf(start)} &gt; {@code input.indexOf(end)}</li>
     * </ol>
     */
    public static String stringAfter(String input, String start) {
        if (input == null || input.length() == 0) {
            return input;
        }

        int startLength = start.length();

        if (startLength >= input.length()) {
            return "";
        }

        int startIndex = input.indexOf(start) + startLength;

        if (startIndex == -1 || startIndex >= input.length()) {
            throw new StringIndexOutOfBoundsException("Start index out of bounds");
        }
        return input.substring(startIndex);
    }

    public static final long ticksPerSecond = 20;

    public static long ticksToMillis(long ticks) {
        return (long)((1000d / ticksPerSecond) * ticks);
    }
    
    public static Time getTimeFromMillis(long millis){
        //idrk why I made this method
        return new Time(millis);
    }
}
