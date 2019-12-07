package main.java.dp.plugin.utils;

import java.util.ArrayList;
import java.util.List;
import static main.java.dp.plugin.maps.EnchantmentData.enchantmentLevelPrefix;
import static main.java.dp.plugin.maps.EnchantmentData.enchantmentNamePrefix;
import static main.java.dp.plugin.maps.EnchantmentData.intToRomanNumeral;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
    @author Daniel Allen
    25-Nov-2019
*/
public class Utils {
    public static double bpsToMovementSpeed(double bps){
        return (100000*bps+2141)/4317800;
    }
    public static ItemStack createEnchantedItem(Material mat, String name, int count, Object[][] enchs, boolean unbreakable, boolean showEnchants) {
        ItemStack output = new ItemStack(mat, count);
        ItemMeta meta = output.getItemMeta();
        List<String> lore = new ArrayList<>();
        meta.setDisplayName(name);
        meta.setUnbreakable(unbreakable);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        if (enchs != null && enchs.length > 0) {
            boolean hasValidEnchant = false;
            for (Object[] o : enchs) {
                if (o[0] instanceof Enchantment) {
                    hasValidEnchant = true;
                    int level;
                    try {
                        level = (int) o[1];
                    } catch (ClassCastException e) {
                        level = 1;
                    }
                    meta.addEnchant((Enchantment) o[0], level, true);
                    if (showEnchants) {
                        lore.add(enchantmentNamePrefix + WordUtils.capitalizeFully((((Enchantment) o[0]).getKey().getKey()), new char[]{' ', '_'}).replaceAll("_", " ") + enchantmentLevelPrefix + intToRomanNumeral(level));
                    }
                }
            }
            if (hasValidEnchant) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.setLore(lore);
            }
        } else if (!showEnchants) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        output.setItemMeta(meta);
        return output;
    }
}
