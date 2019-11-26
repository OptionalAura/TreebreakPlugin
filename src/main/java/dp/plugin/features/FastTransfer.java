package main.java.dp.plugin.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import main.java.dp.plugin.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
    @author Daniel Allen
    18-Aug-2019
 */
public class FastTransfer implements Listener {

   /* boolean needsDebug = false;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        if (Main.dataHandler.debugMode == null) {
            Main.dataHandler.debugMode = false;
        }
        if (Main.dataHandler.fastEnabled == null) {
            Main.dataHandler.fastEnabled = new HashMap<>();
        }
        if (Main.dataHandler.debugMode && needsDebug) {
            player.sendMessage("Debug: A");
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (Main.dataHandler.debugMode && needsDebug) {
                player.sendMessage("Debug: B");
            }
            if (event.getPlayer().isSneaking()) {
                if (Main.dataHandler.debugMode && needsDebug) {
                    player.sendMessage("Debug: C");
                }
                if (Main.dataHandler.fastEnabled.containsKey(player.getUniqueId())) {
                    if (Main.dataHandler.debugMode && needsDebug) {
                        player.sendMessage("Debug: D");
                    }
                    if (Main.dataHandler.debugMode && needsDebug) {
                        player.sendMessage("Debug: playerID: " + player.getUniqueId() + "; Value = " + Main.dataHandler.fastEnabled.get(player.getUniqueId()));
                    }
                    if (Main.dataHandler.fastEnabled.get(player.getUniqueId()) == true) {
                        if (Main.dataHandler.debugMode && needsDebug) {
                            player.sendMessage("Debug: E");
                        }
                        Block block = event.getClickedBlock();
                        if (block == null) {
                            return;
                        }
                        if (block.getType() == Material.CHEST) {
                            event.setCancelled(true);
                            Chest chest = (Chest) block.getState();
                            Material transferItem = event.getMaterial();
                            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                                ItemStack is = player.getInventory().getContents()[i];
                                if (is != null) {
                                    if (is.getType() == transferItem) {
                                        if (chest.getInventory().firstEmpty() != -1) {
                                            chest.getInventory().setItem(chest.getInventory().firstEmpty(), is);
                                            player.getInventory().clear(i);
                                        } else {
                                            return;
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (Main.dataHandler.inventoryDoubleClicks == null) {
            Main.dataHandler.inventoryDoubleClicks = new HashMap<>();
        }
        if (Main.dataHandler.fastEnabled == null) {
            Main.dataHandler.fastEnabled = new HashMap<>();
        }
        if (!Main.dataHandler.fastEnabled.containsKey(e.getWhoClicked().getUniqueId())) {
            Main.dataHandler.fastEnabled.put(e.getWhoClicked().getUniqueId(), false);
        }
        if (Main.dataHandler.fastEnabled.get(e.getWhoClicked().getUniqueId()) == true && e.isShiftClick() && e.isLeftClick()) {
            e.setCancelled(true);
            if (e.getInventory().getType() == InventoryType.CHEST || e.getInventory().getType() == InventoryType.ENDER_CHEST || e.getInventory().getType() == InventoryType.SHULKER_BOX) {
                if (e.getClickedInventory() == null) {
                    return;
                }
                if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
                    if (e.getInventory().getType() == InventoryType.PLAYER) {
                        return;
                    }
                    //transfer from player to storage
                    ItemStack targetItem = e.getCurrentItem();
                    if (targetItem == null) {
                        return;
                    }
                    for (int i = 0; i < e.getClickedInventory().getSize(); i++) {
                        ItemStack curItem = e.getClickedInventory().getItem(i);
                        if (curItem == null) {
                            continue;
                        }
                        if (curItem.getType() == targetItem.getType()) {
                            if (e.getInventory().firstEmpty() != -1) {
                                e.getInventory().setItem(e.getInventory().firstEmpty(), curItem);
                                e.getClickedInventory().clear(i);
                            }
                        }
                    }
                } else {
                    //transfer from storage to player
                    ItemStack targetItem = e.getCurrentItem();
                    if (targetItem == null) {
                        return;
                    }
                    for (int i = 0; i < e.getInventory().getSize(); i++) {
                        ItemStack curItem = e.getInventory().getItem(i);
                        if (curItem == null) {
                            continue;
                        }
                        if (curItem.getType() == targetItem.getType()) {
                            if (e.getWhoClicked().getInventory().firstEmpty() != -1) {
                                e.getWhoClicked().getInventory().setItem(e.getWhoClicked().getInventory().firstEmpty(), curItem);
                                e.getInventory().clear(i);
                            }
                        }
                    }
                }
            }
            return;
        }
        if (e.getInventory().getType() == InventoryType.CHEST || e.getInventory().getType() == InventoryType.ENDER_CHEST || e.getInventory().getType() == InventoryType.SHULKER_BOX) {
            if (Main.dataHandler.debugMode && needsDebug) {
                e.getWhoClicked().sendMessage("Debug: A");
            }
            if (e.isShiftClick() && e.isRightClick()) {
                if (Main.dataHandler.debugMode && needsDebug) {
                    e.getWhoClicked().sendMessage("Debug: B");
                }
                e.setCancelled(true);
                UUID playerID = e.getWhoClicked().getUniqueId();
                ArrayList<Integer> clicks = new ArrayList<>();
                if (!Main.dataHandler.inventoryDoubleClicks.containsKey(playerID)) {
                    if (Main.dataHandler.debugMode && needsDebug) {
                        e.getWhoClicked().sendMessage("Debug: C.1");
                    }
                    clicks.add(e.getSlot());
                    Main.dataHandler.inventoryDoubleClicks.put(playerID, clicks);
                    return;
                } else {
                    if (Main.dataHandler.debugMode && needsDebug) {
                        e.getWhoClicked().sendMessage("Debug: C.2");
                    }
                    clicks = Main.dataHandler.inventoryDoubleClicks.get(playerID);
                    clicks.add(e.getSlot());
                }
                if (Main.dataHandler.debugMode && needsDebug) {
                    e.getWhoClicked().sendMessage("Debug: D");
                }
                if (clicks.size() < 2) {
                    return;
                }
                if (Main.dataHandler.debugMode && needsDebug) {
                    e.getWhoClicked().sendMessage("Debug: E");
                }
                int click1 = clicks.get(0);
                int click2 = clicks.get(1);
                for (int i = Math.min(click1, click2); i <= Math.max(click1, click2); i++) {
                    if (Main.dataHandler.debugMode && needsDebug) {
                        e.getWhoClicked().sendMessage("Debug: F." + (i - (Math.min(click1, click2)) + 1));
                    }
                    ItemStack curItem = e.getInventory().getItem(i);
                    if (curItem == null) {
                        continue;
                    }
                    if (Main.dataHandler.debugMode && needsDebug) {
                        e.getWhoClicked().sendMessage("Debug: Item: " + curItem.getType().toString());
                    }
                    if (e.getWhoClicked().getInventory().firstEmpty() != -1) {
                        if (Main.dataHandler.debugMode && needsDebug) {
                            e.getWhoClicked().sendMessage("Debug: G." + (i - (Math.min(click1, click2)) + 1));
                        }
                        e.getInventory().clear(i);
                        e.getWhoClicked().getInventory().setItem(e.getWhoClicked().getInventory().firstEmpty(), curItem);
                        if (Main.dataHandler.debugMode && needsDebug) {
                            e.getWhoClicked().sendMessage("Debug: H." + (i - (Math.min(click1, click2)) + 1));
                        }
                    }
                }
                if (Main.dataHandler.debugMode && needsDebug) {
                    e.getWhoClicked().sendMessage("Debug: I");
                }
                clicks.clear();
                Main.dataHandler.inventoryDoubleClicks.remove(playerID);
            }
        }
    }
*/
}
