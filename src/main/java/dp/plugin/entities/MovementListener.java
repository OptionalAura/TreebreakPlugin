/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.dp.plugin.entities;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.util.ArrayList;
import main.java.dp.plugin.Main;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.Position;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 *
 * @author Daniel Allen
 */
public abstract class MovementListener extends CraftEntity{

    public MovementListener(CraftServer server, Entity entity) {
        super(server, entity);
        BukkitTask tick = new BukkitRunnable() {
            @Override
            public void run() {
                tick();
            }
        }.runTaskTimer(Main.thisPlugin, 0, 1);
    }

    public abstract void tick();

    public void registerEntity(Entity e){

    }

    public void move(Location from, Location to){
        Bukkit.getPluginManager().callEvent(new EntityMoveEvent(this, from, to));
    }

    public abstract EntityType getType();

}
