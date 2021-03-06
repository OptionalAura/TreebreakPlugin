/*
 * Copyright (C) 2021 Daniel Allen
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the
 * GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.treebreaker.plugin.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Daniel Allen
 */
public class Utils {

    private static final ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

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

    public static boolean hasProperty(String property) {
        return data.get(property) != null;
    }

    public static String getAllProperties() {
        return joinList(new ArrayList(data.keySet()));
    }

    public static Set<String> getAllPropertyKeys() {
        return data.keySet();
    }

    public static String joinList(Collection list) {
        if (list.size() > 0) {
            StringBuilder sb = new StringBuilder(list.size());
            sb.append('[');
            Iterator it = list.iterator();
            for (int z = 0; z < list.size() - 1; z++) {
                sb.append(it.next());
                sb.append(',').append(' ');
            }
            sb.append(it.next());
            sb.append(']');
            return sb.toString();
        }
        return "[]";
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
     *
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

        if (start.length() >= input.length()) {
            return "";
        }

        int startIndex = input.indexOf(start) + start.length();

        if (startIndex == -1 || startIndex >= input.length()) {
            throw new StringIndexOutOfBoundsException("Start index out of bounds");
        }
        return input.substring(startIndex);
    }

    public static final long ticksPerSecond = 20;

    public static long ticksToMillis(long ticks) {
        return (long) ((1000d / ticksPerSecond) * ticks);
    }

    public static Time getTimeFromMillis(long millis) {
        //idrk why I made this method
        return new Time(millis);
    }

    public static boolean startsWithIgnoreCase(String full, String test) {
        return (full == null) == (test == null) && full != null && test.length() <= full.length() && full.substring(0, test.length()).equalsIgnoreCase(test);
    }
    public static final ArrayList<Enchantment> enchList = new ArrayList<>();
    public static final ArrayList<Enchantment> restrictedEnchantments = new ArrayList<>();
    public static Comparator enchSorterByLevel;
    private final static TreeMap<Integer, String> romanMap = new TreeMap<Integer, String>();

    static {
        //what enchantments can only be level 1
        restrictedEnchantments.add(Enchantment.WATER_WORKER);
        restrictedEnchantments.add(Enchantment.CHANNELING);
        restrictedEnchantments.add(Enchantment.BINDING_CURSE);
        restrictedEnchantments.add(Enchantment.VANISHING_CURSE);
        restrictedEnchantments.add(Enchantment.WATER_WORKER);
        restrictedEnchantments.add(Enchantment.ARROW_FIRE);
        restrictedEnchantments.add(Enchantment.ARROW_INFINITE);
        restrictedEnchantments.add(Enchantment.MULTISHOT);
        restrictedEnchantments.add(Enchantment.SILK_TOUCH);

        //list of all enchantments
        enchList.add(Enchantment.ARROW_DAMAGE);
        enchList.add(Enchantment.ARROW_FIRE);
        enchList.add(Enchantment.ARROW_INFINITE);
        enchList.add(Enchantment.ARROW_KNOCKBACK);
        enchList.add(Enchantment.CHANNELING);
        enchList.add(Enchantment.DAMAGE_ALL);
        enchList.add(Enchantment.DAMAGE_ARTHROPODS);
        enchList.add(Enchantment.DAMAGE_UNDEAD);
        enchList.add(Enchantment.DEPTH_STRIDER);
        enchList.add(Enchantment.DIG_SPEED);
        enchList.add(Enchantment.DURABILITY);
        enchList.add(Enchantment.FIRE_ASPECT);
        enchList.add(Enchantment.FROST_WALKER);
        enchList.add(Enchantment.IMPALING);
        enchList.add(Enchantment.KNOCKBACK);
        enchList.add(Enchantment.LOOT_BONUS_BLOCKS);
        enchList.add(Enchantment.LOOT_BONUS_MOBS);
        enchList.add(Enchantment.LOYALTY);
        enchList.add(Enchantment.LUCK);
        enchList.add(Enchantment.LURE);
        enchList.add(Enchantment.MENDING);
        enchList.add(Enchantment.OXYGEN);
        enchList.add(Enchantment.PROTECTION_ENVIRONMENTAL);
        enchList.add(Enchantment.PROTECTION_EXPLOSIONS);
        enchList.add(Enchantment.PROTECTION_FALL);
        enchList.add(Enchantment.PROTECTION_FIRE);
        enchList.add(Enchantment.PROTECTION_PROJECTILE);
        enchList.add(Enchantment.RIPTIDE);
        enchList.add(Enchantment.SILK_TOUCH);
        enchList.add(Enchantment.SWEEPING_EDGE);
        enchList.add(Enchantment.THORNS);
        enchList.add(Enchantment.WATER_WORKER);

        //Roman Numeral Mappings
        romanMap.put(1000, "M");
        romanMap.put(900, "CM");
        romanMap.put(500, "D");
        romanMap.put(400, "CD");
        romanMap.put(100, "C");
        romanMap.put(90, "XC");
        romanMap.put(50, "L");
        romanMap.put(40, "XL");
        romanMap.put(10, "X");
        romanMap.put(9, "IX");
        romanMap.put(5, "V");
        romanMap.put(4, "IV");
        romanMap.put(1, "I");
    }

    public static String intToRomanNumeral(int in) {
        int l = romanMap.floorKey(in);
        if (in == 0) {
            return "nulla";
        }
        if (in == l) {
            return romanMap.get(in);
        }
        return romanMap.get(l) + intToRomanNumeral(in - l);
    }

    public static int romanNumeralToInt(String rn) {
        if (rn == null) {
            return 0;
        }
        if (rn.isEmpty()) {
            return 0;
        }
        int value = 0;
        int pre = 0;
        Set<Entry<Integer, String>> keyVals = romanMap.entrySet();
        for (int i = rn.length() - 1; i >= 0; i--) {
            int curValue = 0;
            for (Entry<Integer, String> ent : keyVals) {
                if (rn.charAt(i) == ent.getValue().toCharArray()[0]) {
                    curValue = ent.getKey();
                }
            }
            if (i == rn.length() - 1) {
                value += curValue;
            } else {
                if (curValue < pre) {
                    value -= curValue;
                } else {
                    value += curValue;
                }
            }
            pre = curValue;
        }

        return value;
    }

    public static Vector randomizeDirection(Vector in, double randomness) {

        if (randomness == 0) {
            return in;
        }
        double theta = Math.random() * Math.PI * 2;
        double v = Math.random();
        double phi = Math.acos((2 * v) - 1);
        double radius = Math.cbrt(Math.random());
        double xDif = radius * Math.sin(phi) * Math.cos(theta);
        double yDif = radius * Math.sin(phi) * Math.sin(theta);
        double zDif = radius * Math.cos(phi);
        return new Vector(in.getX() + xDif * randomness, in.getY() + yDif * randomness, in.getZ() + zDif * randomness);

    }
    public static Vector randomizeDirectionFast(Vector in, double randomness){
        //This has a terrible worst-case scenario but is on average much faster than the above. Seems to be less uniform.
        if (randomness == 0 || in == null) {
            return in;
        }

        double xDif = (Math.random() - 0.5) * 2;
        double yDif = (Math.random() - 0.5) * 2;
        double zDif = (Math.random() - 0.5) * 2;
        if (xDif * xDif + yDif * yDif + zDif * zDif < 1) {
            return randomizeDirectionFast(in, randomness);
        } else {
            return new Vector(in.getX() + xDif * randomness, in.getY() + yDif * randomness, in.getZ() + zDif * randomness);
        }
    }
    public static String enchantmentLevelPrefix = "§r§9 ";
    public static String enchantmentNamePrefix = "§r§a";
}
