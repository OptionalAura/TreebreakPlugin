package main.java.dp.plugin.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*
    @author Daniel Allen
    3-Dec-2019
 */
public class EntityMoveEvent extends Event implements Cancellable {

    private boolean cancelled = false;
    private Entity entity;
    private Location init;
    private Location end;
    public EntityMoveEvent(Entity e, Location init, Location end){
        this.entity = e;
        this.init = init;
        this.end = end;
    }
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    public Entity getEntity(){
        return this.entity;
    }
    public Location getInitalLocation(){
        return this.init;
    }
    public Location getFinalLocation(){
        return this.end;
    }
}
