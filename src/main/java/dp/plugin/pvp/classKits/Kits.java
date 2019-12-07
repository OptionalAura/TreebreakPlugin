package main.java.dp.plugin.pvp.classKits;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import static main.java.dp.plugin.Main.thisPlugin;
import static main.java.dp.plugin.utils.Utils.createEnchantedItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/*
    @author Daniel Allen
    24-Nov-2019
 */
public class Kits {

    public static List<Kit> kits = new ArrayList<>();
    public static final Kit warrior, archer, railgunner, mage, boomer;

    static {
        //warrior
        ItemStack warriorLabelItem = createEnchantedItem(Material.IRON_SWORD, ChatColor.RESET + "Warrior", 1, null, true, true);
        ItemStack warriorHelmetItem = createEnchantedItem(Material.IRON_HELMET, ChatColor.RESET + "Warrior Helmet", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 4}}, true, true);
        ItemStack warriorChestplateItem = createEnchantedItem(Material.IRON_CHESTPLATE, ChatColor.RESET + "Warrior Chestplate", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 4}}, true, true);
        ItemStack warriorLeggingsItem = createEnchantedItem(Material.IRON_LEGGINGS, ChatColor.RESET + "Warrior Leggings", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 4}}, true, true);
        ItemStack warriorBootsItem = createEnchantedItem(Material.IRON_BOOTS, ChatColor.RESET + "Warrior Boots", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 4}}, true, true);

        TreeMap<Integer, ItemStack> warriorInventory = new TreeMap<>();
        warriorInventory.put(0, createEnchantedItem(Material.IRON_SWORD, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "Warrior Sword", 1, new Object[][]{{Enchantment.DAMAGE_ALL, 2}}, true, true));

        //archer
        ItemStack archerLabelItem = createEnchantedItem(Material.BOW, ChatColor.RESET + "Archer", 1, null, true, true);
        ItemStack archerHelmetItem = createEnchantedItem(Material.LEATHER_HELMET, ChatColor.RESET + "Archer Helmet", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 3}}, true, true);
        ItemStack archerChestplateItem = createEnchantedItem(Material.LEATHER_CHESTPLATE, ChatColor.RESET + "Archer Chestplate", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 3}}, true, true);
        ItemStack archerLeggingsItem = createEnchantedItem(Material.LEATHER_LEGGINGS, ChatColor.RESET + "Archer Leggings", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 3}}, true, true);
        ItemStack archerBootsItem = createEnchantedItem(Material.LEATHER_BOOTS, ChatColor.RESET + "Archer Boots", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 3}}, true, true);

        TreeMap<Integer, ItemStack> archerInventory = new TreeMap<>();
        archerInventory.put(0, createEnchantedItem(Material.WOODEN_SWORD, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "Archer Sword", 1, null, true, true));
        archerInventory.put(1, createEnchantedItem(Material.BOW, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "Archer Bow", 1, new Object[][]{{Enchantment.ARROW_DAMAGE, 2}, {Enchantment.ARROW_INFINITE, 1}}, true, true));
        archerInventory.put(9, new ItemStack(Material.ARROW, 1));

        //railgunner
        ItemStack railgunnerLabelItem = createEnchantedItem(Material.STICK, ChatColor.RESET + "Railgunner", 1, new Object[][]{{Enchantment.DURABILITY, 1}}, true, false);
        ItemStack railgunnerHelmetItem = createEnchantedItem(Material.CHAINMAIL_HELMET, ChatColor.RESET + "Railgunner Helmet", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 2}}, true, true);
        ItemStack railgunnerChestplateItem = createEnchantedItem(Material.CHAINMAIL_CHESTPLATE, ChatColor.RESET + "Railgunner Chestplate", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 2}}, true, true);
        ItemStack railgunnerLeggingsItem = createEnchantedItem(Material.CHAINMAIL_LEGGINGS, ChatColor.RESET + "Railgunner Leggings", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 2}}, true, true);
        ItemStack railgunnerBootsItem = createEnchantedItem(Material.CHAINMAIL_BOOTS, ChatColor.RESET + "Railgunner Boots", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 2}}, true, true);

        ItemStack railgun = createEnchantedItem(Material.STICK, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "Railgun", 1, null, true, true);
        ItemMeta railgunMeta = railgun.getItemMeta();
        NamespacedKey key = new NamespacedKey(thisPlugin, "weapontype");
        railgunMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 2);
        railgun.setItemMeta(railgunMeta);

        TreeMap<Integer, ItemStack> railgunnerInventory = new TreeMap<>();
        railgunnerInventory.put(0, createEnchantedItem(Material.WOODEN_SWORD, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "Railgunner Sword", 1, null, true, true));
        railgunnerInventory.put(1, railgun);

        //mage
        ItemStack mageLabelItem = createEnchantedItem(Material.STICK, ChatColor.RESET + "Mage", 1, new Object[][]{{Enchantment.DURABILITY, 1}}, true, false);
        ItemStack mageHelmetItem = createEnchantedItem(Material.CHAINMAIL_HELMET, ChatColor.RESET + "Mage Helmet", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 2}}, true, true);
        ItemStack mageChestplateItem = createEnchantedItem(Material.CHAINMAIL_CHESTPLATE, ChatColor.RESET + "Mage Chestplate", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 2}}, true, true);
        ItemStack mageLeggingsItem = createEnchantedItem(Material.CHAINMAIL_LEGGINGS, ChatColor.RESET + "Mage Leggings", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 2}}, true, true);
        ItemStack mageBootsItem = createEnchantedItem(Material.CHAINMAIL_BOOTS, ChatColor.RESET + "Mage Boots", 1, new Object[][]{{Enchantment.PROTECTION_ENVIRONMENTAL, 2}}, true, true);

        ItemStack wand = createEnchantedItem(Material.STICK, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "Wand", 1, null, true, true);
        ItemMeta wandMeta = wand.getItemMeta();
        wandMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 3);
        wand.setItemMeta(wandMeta);

        TreeMap<Integer, ItemStack> mageInventory = new TreeMap<>();
        mageInventory.put(0, createEnchantedItem(Material.WOODEN_SWORD, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "Mage Sword", 1, null, true, true));
        mageInventory.put(1, wand);

        //boomer
        ItemStack boomerLabelItem = createEnchantedItem(Material.TNT, ChatColor.RESET + "Boomer", 1, new Object[][]{{Enchantment.DURABILITY, 1}}, true, false);
        ItemStack boomerHelmetItem = createEnchantedItem(Material.CHAINMAIL_HELMET, ChatColor.RESET + "Boomer Helmet", 1, new Object[][]{{Enchantment.PROTECTION_EXPLOSIONS, 20}}, true, true);
        ItemStack boomerChestplateItem = createEnchantedItem(Material.CHAINMAIL_CHESTPLATE, ChatColor.RESET + "Boomer Chestplate", 1, new Object[][]{{Enchantment.PROTECTION_EXPLOSIONS, 20}}, true, true);
        ItemStack boomerLeggingsItem = createEnchantedItem(Material.CHAINMAIL_LEGGINGS, ChatColor.RESET + "Boomer Leggings", 1, new Object[][]{{Enchantment.PROTECTION_EXPLOSIONS, 20}}, true, true);
        ItemStack boomerBootsItem = createEnchantedItem(Material.CHAINMAIL_BOOTS, ChatColor.RESET + "Boomer Boots", 1, new Object[][]{{Enchantment.PROTECTION_EXPLOSIONS, 20}}, true, true);

        ItemStack tnt = createEnchantedItem(Material.TNT, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "TNT", 32, new Object[][]{}, true, true);
        ItemMeta tntMeta = tnt.getItemMeta();
        tntMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 4);
        tnt.setItemMeta(tntMeta);

        TreeMap<Integer, ItemStack> boomerInventory = new TreeMap<>();
        boomerInventory.put(0, createEnchantedItem(Material.WOODEN_SWORD, ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "Boomer Sword", 1, null, true, true));
        boomerInventory.put(1, tnt);

        warrior = new Kit(2, warriorLabelItem, new ItemStack[]{warriorHelmetItem, warriorChestplateItem, warriorLeggingsItem, warriorBootsItem}, warriorInventory, null);
        archer = new Kit(3, archerLabelItem, new ItemStack[]{archerHelmetItem, archerChestplateItem, archerLeggingsItem, archerBootsItem}, archerInventory, null);
        boomer = new Kit(4, boomerLabelItem, new ItemStack[]{boomerHelmetItem, boomerChestplateItem, boomerLeggingsItem, boomerBootsItem}, boomerInventory, null);
        mage = new Kit(5, mageLabelItem, new ItemStack[]{mageHelmetItem, mageChestplateItem, mageLeggingsItem, mageBootsItem}, mageInventory, null);
        railgunner = new Kit(6, railgunnerLabelItem, new ItemStack[]{railgunnerHelmetItem, railgunnerChestplateItem, railgunnerLeggingsItem, railgunnerBootsItem}, railgunnerInventory, null);

        kits.add(warrior);
        kits.add(archer);
        kits.add(boomer);
        kits.add(mage);
        kits.add(railgunner);
    }
}
