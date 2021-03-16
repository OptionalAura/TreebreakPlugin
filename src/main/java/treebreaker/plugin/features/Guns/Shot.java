/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dsato
 */
public class Shot {

    private final List<Projectile> projectileBackend;
    private final List<Projectile> projectiles;
    private int count = 0;
    public boolean alive = true;

    public Shot(int count) {
        projectileBackend = new ArrayList<>(count);
        projectiles = Collections.synchronizedList(projectileBackend);
        //projectiles.ensureCapacity(count);
    }

    public void add(Projectile p) {
        count++;
        projectiles.add(p);
    }

    public void tick() {
        synchronized(projectiles){
            for (int i = 0; i < projectiles.size(); i++) {
                Projectile p = projectiles.get(i);
                if (p.isAlive()) {
                    p.tick();

                }
            }
        }
    }

    public void decrement(){
        count--;
        if (count == 0) {
            alive = false;
            projectiles.clear();
        }
    }
    
    public int getCount(){
        return this.count;
    }
    
    public boolean isAlive() {
        return this.alive;
    }
}
