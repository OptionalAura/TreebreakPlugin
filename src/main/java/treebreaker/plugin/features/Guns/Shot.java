/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.ArrayList;

/**
 *
 * @author dsato
 */
public class Shot {

    private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    private int count = 0;
    public boolean alive = true;

    public Shot(int count) {
        projectiles.ensureCapacity(count);
    }

    public void add(Projectile p) {
        count++;
        projectiles.add(p);
    }

    public void tick() {
        for (Projectile p : projectiles) {
            if (p.isAlive()) {
                p.tick();
                
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
    
    public boolean isAlive() {
        return this.alive;
    }
}
