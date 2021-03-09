/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.misc;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author dsato
 */
public class ShootEvent extends org.bukkit.event.Event {
    enum WeaponType{
        SHOTGUN
    }
    public ShootEvent(Player shooter, ItemStack item, int WeaponType) {
        
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
