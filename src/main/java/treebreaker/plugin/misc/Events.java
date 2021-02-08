/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import main.java.treebreaker.plugin.Main;
import static main.java.treebreaker.plugin.Main.updateAvailable;
import static main.java.treebreaker.plugin.Main.updateMessage;
import main.java.treebreaker.plugin.utils.Utils;
import main.java.treebreaker.plugin.utils.Version;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

/**
 *
 * @author dsato
 */
public class Events implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(event.getPlayer().isOp()){
            try {
                URL updateCheckURL = new URL("https://raw.githubusercontent.com/OptionalAura/TreebreakPlugin/master/src/plugin.yml");
                try ( BufferedReader br = new BufferedReader(new InputStreamReader(updateCheckURL.openStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("version: ")) {
                            line = Utils.stringAfter(line, "version: ");
                            Version updatedVersion = new Version(line);
                            if (Main.version.compareTo(updatedVersion) == -1) {
                                updateAvailable = true;
                                updateMessage = "There is an update available for " + Main.thisPlugin.getName() + "(v. " + Main.thisPlugin.getDescription().getVersion() + " -> v. " + line.replaceAll("[^0-9.]", "") + ")";
                                event.getPlayer().sendMessage(ChatColor.RED + updateMessage + ChatColor.RESET);
                                TextComponent updateText = new TextComponent(ChatColor.RED + "Click to update" + ChatColor.RESET);
                                updateText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/OptionalAura/TreebreakPlugin"));
                                event.getPlayer().spigot().sendMessage(updateText);
                            }
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "Error detecting updates for Treebreaker:" + ChatColor.RESET);
                Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + e.getLocalizedMessage() + ChatColor.RESET);
            }
        }
    }
    @EventHandler
    public void onBowShoot(EntityShootBowEvent event){
        if(event.getBow().containsEnchantment(Enchantment.MULTISHOT)){
            int arrowCount = Math.min(event.getBow().getEnchantmentLevel(Enchantment.MULTISHOT) * 2,50);
            double radSpread = 0.2;
            double spacing = radSpread/arrowCount;
            double yaw = event.getEntity().getLocation().getYaw()-radSpread;
            Vector facing = event.getEntity().getLocation().getDirection();
            for(int i = 0; i < arrowCount/2; i++){
                facing.rotateAroundY(-spacing);
                Arrow spawned = event.getEntity().getWorld().spawnArrow(event.getProjectile().getLocation(), facing, (float)event.getProjectile().getVelocity().length(), 12);
                spawned.setShooter(event.getEntity());
                spawned.setDamage(((Arrow)event.getProjectile()).getDamage());
                spawned.setPickupStatus(PickupStatus.CREATIVE_ONLY);
                if(((Arrow)event.getProjectile()).hasCustomEffects())
                    spawned.setBasePotionData(((Arrow)event.getProjectile()).getBasePotionData());
                spawned.setFireTicks(((Arrow)event.getProjectile()).getFireTicks());
                spawned.setKnockbackStrength(((Arrow)event.getProjectile()).getKnockbackStrength());
                spawned.setPierceLevel(((Arrow)event.getProjectile()).getPierceLevel());
            }
            facing = event.getEntity().getLocation().getDirection();
            for(int i = 0; i < arrowCount/2; i++){
                facing.rotateAroundY(spacing);
                Arrow spawned = event.getEntity().getWorld().spawnArrow(event.getProjectile().getLocation(), facing, (float)event.getProjectile().getVelocity().length(), 12);
                spawned.setShooter(event.getEntity());
                spawned.setDamage(((Arrow)event.getProjectile()).getDamage());
                spawned.setPickupStatus(PickupStatus.CREATIVE_ONLY);
                if(((Arrow)event.getProjectile()).hasCustomEffects())
                    spawned.setBasePotionData(((Arrow)event.getProjectile()).getBasePotionData());
                spawned.setFireTicks(((Arrow)event.getProjectile()).getFireTicks());
                spawned.setKnockbackStrength(((Arrow)event.getProjectile()).getKnockbackStrength());
                spawned.setPierceLevel(((Arrow)event.getProjectile()).getPierceLevel());
            }
        }
    }
}
