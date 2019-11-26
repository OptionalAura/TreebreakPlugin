package main.java.dp.plugin.maps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

/*
    @author Daniel Allen
    23-Aug-2019
 */
public class EnchantmentData {

    public static final ArrayList<Enchantment> enchList = new ArrayList<>();
    public static final ArrayList<Enchantment> restrictedEnchantments = new ArrayList<>();
    public static Comparator enchSorterByLevel;
    private final static TreeMap<Integer, String> romanMap = new TreeMap<Integer, String>();

    static{
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
    public static int romanNumeralToInt(String rn){
        if(rn == null)
            return 0;
        if(rn.isEmpty())
            return 0;
        int value = 0;
        int pre = 0;
        Set<Entry<Integer, String>> keyVals = romanMap.entrySet();
        for(int i = rn.length()-1; i >= 0; i--){
            int curValue = 0;
            for(Entry<Integer, String> ent : keyVals){
                if(rn.charAt(i) == ent.getValue().toCharArray()[0]){
                    curValue = ent.getKey();
                }
            }
            if(i == rn.length()-1){
                value += curValue;
            }else{
                if(curValue < pre){
                    value -= curValue;
                }else{
                    value += curValue;
                }
            }
            pre = curValue;
        }

        return value;
    }
    /*
    check if next char is '
        if it is, check the next char
        if the next ch
    */
    public static String enchantmentLevelPrefix = "§r§9 ";
    public static String enchantmentNamePrefix = "§r§a";
}
