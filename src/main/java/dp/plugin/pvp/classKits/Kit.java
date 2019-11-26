package main.java.dp.plugin.pvp.classKits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

/*
    @author Daniel Allen
    24-Nov-2019
 */
public class Kit {

    private ItemStack labelItem;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private int position;
    private TreeMap<Integer, ItemStack> inventory;
    private List<PotionEffect> effects;

    public Kit(int position, ItemStack labelItem, ItemStack[] armorItems, Map<Integer, ItemStack> inventory, List<PotionEffect> effects) {
        this.position = position;

        this.labelItem = labelItem;

        this.boots = armorItems[0] == null ? new ItemStack(Material.AIR) : armorItems[0];
        this.leggings = armorItems[1] == null ? new ItemStack(Material.AIR) : armorItems[1];
        this.chestplate = armorItems[2] == null ? new ItemStack(Material.AIR) : armorItems[2];
        this.helmet = armorItems[3] == null ? new ItemStack(Material.AIR) : armorItems[3];
        this.inventory = (TreeMap<Integer, ItemStack>) (inventory == null ? new TreeMap<>() : inventory);
        this.effects = effects == null ? new ArrayList<PotionEffect>() : effects;
    }

    public int getPosition() {
        return this.position;
    }

    public void setArmorItems(ItemStack[] armorItems) {
        this.helmet = armorItems[0];
        this.chestplate = armorItems[1];
        this.leggings = armorItems[2];
        this.boots = armorItems[3];
    }

    public void setInventoryItems(TreeMap<Integer, ItemStack> inventory) {
        this.inventory = inventory;
    }

    public void addEffect(PotionEffect effect) {
        effects.add(effect);
    }

    public Inventory applyToPlayer(Player p, boolean msg) {
        Inventory before = p.getInventory();
        p.getInventory().clear();
        p.getActivePotionEffects().clear();
        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        p.setExhaustion(0);
        p.setFoodLevel(20);
        p.getInventory().setArmorContents(new ItemStack[]{helmet, chestplate, leggings, boots});
        Set<Map.Entry<Integer, ItemStack>> itemsInInventory = inventory.entrySet();
        for (Map.Entry<Integer, ItemStack> i : itemsInInventory) {
            p.getInventory().setItem(i.getKey(), i.getValue());
        }
        if (msg) {
            p.sendMessage("Selected " + labelItem.getItemMeta().getDisplayName());
        }
        return before;
    }

    public ItemStack getLabelItem() {
        return this.labelItem;
    }
}
