/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author dsato
 */
public class Shotgun extends Gun{
    private static final int count = 82;
    private static final double velocity = 5;
    private static final double spread = 2;
    public static void shoot(Player shooter, ItemStack item, int val){
        Location playerPos = shooter.getLocation();
        
        Shot shot = new Shot(count);
        for(int i = 0; i < count; i++){
            Location newPos = playerPos.clone();
            Vector dir = newPos.getDirection();
            dir.setX(dir.getX() + (Math.random()-0.5)*spread);
            dir.setY(dir.getY() + (Math.random()-0.5)*spread);
            dir.setZ(dir.getZ() + (Math.random()-0.5)*spread);
            dir.normalize();
            newPos.setDirection(dir);
            Projectile p = new Projectile(newPos, velocity, 5, shooter, shot);
            shot.add(p);
        }
        shots.add(shot);
    }
}
