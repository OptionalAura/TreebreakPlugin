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
public class Gun {
    protected static ArrayList<Shot> shots;
    private static long tickCount = 0;
    public static void tick(){
        tickCount++;
        for(Shot shot : shots){
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
