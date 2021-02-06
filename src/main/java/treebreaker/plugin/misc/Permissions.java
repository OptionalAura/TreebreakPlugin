package main.java.treebreaker.plugin.misc;

import java.util.HashMap;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author dsato
 */
public class Permissions {
    static PluginManager pm;
    static HashMap<String, Permission> perms = new HashMap();
    public static void load(){
        pm = getServer().getPluginManager();
        definePerm("TreeEnchants");
    }
    public static void definePerm(String name){
        if(!perms.containsKey(name)){
            Permission perm = new Permission(name);
            perms.put(name, perm);
            pm.addPermission(perm);
        }
    }
    public static Permission getPerm(String name){
        return perms.get(name);
    }
}
