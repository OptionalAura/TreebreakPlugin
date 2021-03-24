/*
 * Copyright (C) 2021 Daniel Allen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main.java.treebreaker.plugin.features.Guns;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Daniel Allen
 */
public class Shot implements Cloneable{

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
