/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author dsato
 */
public abstract class Gun {
    protected static final List<Shot> shotsBackend = new ArrayList<>();
    protected static final List<Shot> shots = Collections.synchronizedList(shotsBackend);
    protected static int tickCount = 0;
    
    public static final String AT4_EXPLOSION_POWER_TAG = "at4explosionpower";
    
    public static void tick(){
        tickCount++;
        synchronized(shots){
            for (int i = 0; i < shots.size(); i++) {
                Shot shot = shots.get(i);
                if(shot.isAlive()){
                    shot.tick();
                }
            }
            if(tickCount % 100 == 0){
                for(int i = shots.size()-1; i >= 0; i--){
                    if(shots.get(i).isAlive() == false){
                        shots.remove(i);
                    }
                }
            }
        }
    }
    static void shoot(Player shooter, ItemStack item, String uuid){};
}
