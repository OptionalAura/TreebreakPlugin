package main.java.dp.plugin.entities.entities;

import main.java.dp.plugin.entities.MovementListener;
import net.minecraft.server.v1_13_R2.Entity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pose;
import org.bukkit.persistence.PersistentDataContainer;

/*
    @author Daniel Allen
    3-Dec-2019
 */
public class CustomTNT extends MovementListener {

    private double lastX, lastY, lastZ;
    private double curX, curY, curZ;
    private World worldIn;

    public CustomTNT(CraftServer server, Entity entity, World w) {
        super(server, entity);
    }

    public void tick() {
        move(new Location(worldIn, lastX, lastY, lastZ), new Location(worldIn, curX, curY, curZ));
    }

    @Override
    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

    @Override
    public Pose getPose() {
        return Pose.STANDING;
    }

    @Override
    public Spigot spigot() {
        return new Spigot();
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return null;
    }
}
