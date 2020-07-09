/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.InventoryHolder;


/**
 *
 * @author dsato
 */
public class Rename {
    public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof InventoryHolder) {
            //((InventoryHolder) sender).getInventory().get
            return true;
        }
        return false;
    }
}
