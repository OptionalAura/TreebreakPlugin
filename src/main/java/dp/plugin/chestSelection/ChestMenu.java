package main.java.dp.plugin.chestSelection;

import java.util.Arrays;
import main.java.dp.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/*
    @author Daniel Allen
    21-Aug-2019
 */
public class ChestMenu implements Listener {

    protected Inventory chestInv;
    int slots;
    String name;
    OptionClickEventHandler handler;
    Plugin plugin;
    private String[] optionNames;
    private ItemStack[] items;

    public ChestMenu(String name, int slots, OptionClickEventHandler onClick, Plugin plugin) {
        this.name = name;
        this.slots = slots;
        this.handler = onClick;
        this.optionNames = new String[slots];
        this.items = new ItemStack[slots];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void pickupItem(int pos, Player player) {
        player.setItemOnCursor(getOption(pos));
    }

    public ChestMenu setOption(int pos, ItemStack item, String name, String... lore) {
        optionNames[pos] = name;
        items[pos] = setItemNameAndLore(item, name, lore);
        return this;
    }

    private boolean clicked = false;

    public void setClicked(boolean c) {
        clicked = c;
    }

    public boolean clicked() {
        return clicked;
    }

    public void openInventory(Player player) {
        chestInv = Bukkit.createInventory(player, slots, name);
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                chestInv.setItem(i, items[i]);
            }
        }
        player.openInventory(chestInv);
        pInvInit = player.getInventory();
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        items = null;
    }

    public ItemStack getOption(int pos) {
        return chestInv.getItem(pos);
    }

    public PlayerInventory pInvInit;

    @EventHandler(priority = EventPriority.HIGH)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(chestInv)) {
            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getType() != InventoryType.PLAYER) {
                    event.setCancelled(true);
                    int slot = event.getRawSlot();
                    if (slot >= 0 && slot < slots && optionNames[slot] != null) {
                        Plugin plug = this.plugin;
                        OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot], event.getClickedInventory().getType());
                        handler.onOptionClick(e);

                        if (e.willDestroy()) {
                            destroy();
                        }
                    }
                } else {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(chestInv)) {
            CloseEvent e = new CloseEvent((Player) event.getPlayer());
            handler.onClose(e);
        }
        //event.getPlayer().setItemOnCursor(new ItemStack(Material.AIR, 0));
        //event.getPlayer().getInventory().setContents(pInvInit.getContents());
    }

    public interface OptionClickEventHandler {

        public void onOptionClick(OptionClickEvent event);

        public void onClose(CloseEvent event);
    }

    public class CloseEvent {

        private Player player;

        public CloseEvent(Player player) {
            this.player = player;
        }

        public Player getPlayer() {
            return this.player;
        }

        public ItemStack[] getItems() {
            return chestInv.getContents();
        }
    }

    public class OptionClickEvent {

        private InventoryType inventoryType;
        private Player player;
        private int position;
        private String name;
        private boolean close;
        private boolean destroy;

        public OptionClickEvent(Player player, int position, String name, InventoryType it) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
            this.inventoryType = it;
        }

        public int getFirstFreeSlot() {
            return chestInv.firstEmpty();
        }

        public InventoryType getType() {
            return this.inventoryType;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPosition() {
            return position;
        }

        public String getName() {
            return name;
        }

        public void clearSlot(int pos) {
            items[pos] = new ItemStack(Material.AIR, 0);
            optionNames[pos] = "";
            chestInv.setContents(items);
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return destroy;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }

        public ItemStack getItem(int pos) {
            return chestInv.getItem(pos);
        }

        public void setOption(int pos, ItemStack item, String name, String... lore) {
            optionNames[pos] = name;
            items[pos] = setItemNameAndLore(item, name, lore);
            chestInv.setContents(items);
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        if (im != null) {
            im.setDisplayName(name);
            im.setLore(Arrays.asList(lore));
            item.setItemMeta(im);
        }
        return item;
    }
}
