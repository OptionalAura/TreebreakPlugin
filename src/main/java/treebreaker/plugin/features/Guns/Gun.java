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

import main.java.treebreaker.plugin.misc.Events;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static main.java.treebreaker.plugin.misc.Events.i_stickGunHashCode;
import static main.java.treebreaker.plugin.misc.Events.i_stickGunUUID;
import static main.java.treebreaker.plugin.utils.Utils.getProperty;
import static main.java.treebreaker.plugin.utils.Utils.setProperty;

/**
 *
 * @author Daniel Allen
 */
public abstract class Gun {

    protected static final List<Shot> shotsBackend = new ArrayList<>();
    private static final List<Shot> shots = Collections.synchronizedList(shotsBackend);
    protected static int tickCount = 0;
    protected final HashMap<String, Object> customProperties = new HashMap<>();
    public static final ConcurrentHashMap<String, Object> tags = new ConcurrentHashMap<>();
    public static final HashMap<String, Gun> guns = new HashMap<>();

    public static int hash;
    public static void addShot(Shot s){
        shots.add(s);
    }
    public static final int[] purgeShots(){
        int before = shots.size();
        int bullets = 0;
        for(int i = 0; i < shots.size(); i++){
            bullets += shots.get(i).getCount();
        }
        shots.clear();
        int after = shots.size();
        return new int[]{before-after,bullets};
    }
    
    public String getName() {
        return "Gun";
    }

    public void addProperty(FileConfiguration settingsConfig, String key, Object def) {
        String str = "guns." + getName() + "." + key;
        Object val = settingsConfig.get(str, def);
        customProperties.put(str, val);
        setProperty(str, val);
    }

    public abstract double getDefaultVelocity();

    public abstract double getDefaultFireRate();

    public abstract double getDefaultDamage();

    public abstract double getDefaultSpread();

    public List<String> getLore() {
        List<String> itemLore = new ArrayList<>();
        if (getProperty("guns." + getName() + ".firerate", 0d) <= 0) {
            itemLore.add(ChatColor.WHITE + "Fire rate: ∞ rpm" + ChatColor.RESET);
        } else {
            itemLore.add(ChatColor.WHITE + "Fire rate: " + 60 / (getProperty("guns." + getName() + ".firerate", getDefaultFireRate()) / 20) + " rpm" + ChatColor.RESET);
        }
        itemLore.add(ChatColor.WHITE + "Damage: " + getProperty("guns." + getName() + ".damage", getDefaultDamage()) / 2 + ChatColor.RED + " ❤" + ChatColor.RESET);
        itemLore.add(ChatColor.WHITE + "Velocity: " + getProperty("guns." + getName() + ".velocity", getDefaultVelocity()) + " m/s" + ChatColor.RESET);
        return itemLore;
    }

    public static void tick() {
        tickCount++;
        synchronized (shots) {
            for (int i = 0; i < shots.size(); i++) {
                Shot shot = shots.get(i);
                if (shot.isAlive()) {
                    shot.tick();
                }
            }
            if (tickCount % 100 == 0) {
                for (int i = shots.size() - 1; i >= 0; i--) {
                    if (shots.get(i).isAlive() == false) {
                        shots.remove(i);
                    }
                }
            }
        }
    }

    public abstract void shoot(Player shooter, ItemStack item, String uuid);

    public static final ItemStack getGun(String name) {
        Gun gun = guns.get(name.toLowerCase());
        if (gun != null) {
            ItemStack ar = new ItemStack(Material.STICK, 1);
            ItemMeta itemMeta = ar.getItemMeta();
            List<String> itemLore = gun.getLore();
            itemMeta.setLore(itemLore);
            itemMeta.setDisplayName(ChatColor.GOLD + gun.getName() + ChatColor.RESET);
            PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
            pdc.set(Events.i_stickGun, PersistentDataType.STRING, gun.getName().toLowerCase());
            pdc.set(i_stickGunUUID, PersistentDataType.STRING, UUID.randomUUID().toString());
            pdc.set(i_stickGunHashCode, PersistentDataType.INTEGER, gun.versionCode());
            ar.setItemMeta(itemMeta);
            return ar;
        }
        return null;
    }

    abstract void init(FileConfiguration settingsConfig);

    abstract void term(FileConfiguration settingsConfig);

    public static void loadSettings(FileConfiguration settingsConfig) {
        guns.put("at4", new AT4());
        guns.put("sniper", new Sniper());
        guns.put("shotgun", new Shotgun());
        guns.put("ar", new AssaultRifle());
        guns.put("mortar", new Mortar());
        guns.put("hellstorm", new Hellstorm());
        for (Gun gun : guns.values()) {
            setProperty("guns." + gun.getName() + ".velocity", settingsConfig.getDouble("guns." + gun.getName() + ".velocity", gun.getDefaultVelocity()));
            setProperty("guns." + gun.getName() + ".firerate", settingsConfig.getDouble("guns." + gun.getName() + ".firerate", gun.getDefaultFireRate()));
            setProperty("guns." + gun.getName() + ".damage", settingsConfig.getDouble("guns." + gun.getName() + ".damage", gun.getDefaultDamage()));
            setProperty("guns." + gun.getName() + ".spread", settingsConfig.getDouble("guns." + gun.getName() + ".spread", gun.getDefaultSpread()));
            gun.init(settingsConfig);
        }
    }

    public static void saveSettings(FileConfiguration settingsConfig) {
        for (Gun gun : guns.values()) {
            settingsConfig.set("guns." + gun.getName() + ".velocity", getProperty("guns." + gun.getName() + ".velocity", gun.getDefaultVelocity()));
            settingsConfig.set("guns." + gun.getName() + ".firerate", getProperty("guns." + gun.getName() + ".firerate", gun.getDefaultFireRate()));
            settingsConfig.set("guns." + gun.getName() + ".damage", getProperty("guns." + gun.getName() + ".damage", gun.getDefaultDamage()));
            settingsConfig.set("guns." + gun.getName() + ".spread", getProperty("guns." + gun.getName() + ".spread", gun.getDefaultSpread()));
            gun.term(settingsConfig);
        }
    }
    
    protected static int doubleToIntCode(double in){
        long l = Double.doubleToLongBits(in);
        return (int) (l ^ (l >>> 32));
    }
    
    public int versionCode(){
        int result = 17;
        result = result * 31 + getName().hashCode();
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".velocity", getDefaultFireRate()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".firerate", getDefaultFireRate()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".damage", getDefaultFireRate()));
        result = result * 31 + doubleToIntCode(getProperty("guns." + getName() + ".spread", getDefaultFireRate()));
        return result;
    }
}
