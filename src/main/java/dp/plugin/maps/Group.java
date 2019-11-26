package main.java.dp.plugin.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Material;

/*
    @author Daniel Allen
    21-Aug-2019
*/
public class Group {
    private List<Material> components = new ArrayList<>();
    private int order;
    private String groupName;
    public Group(String groupName, int order){
        this.groupName = groupName;
        this.order = order;
    }

    public int getOrder(){
        return order;
    }

    String desc = "";
    public String getDesc(){
        return this.desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    /**
    * Overridden method:
    * @return The name of this group as a String
    */
    @Override
    public String toString(){
        return groupName;
    }
    public List<Material> getComponents(){
        return components;
    }
    public boolean has(Material m){
        return components.contains(m);
    }
    /**
     *
     * @param m Material (Type) to add to this group.
     */
    public void addComponent(Material m){
        components.add(m);
    }

    /**
     *
     * @param materials Array of materials to add to this group.
     *
     * This is the same as same as running each material into addComponent.<br><br>
     * Uses<br>
     *<code>
     *for(Material m : c){<br>
     *&nbsp;&nbsp;&nbsp;&nbsp;addComponent(m);<br>
     *}
     *</code>
     */
    public void addAll(Material... materials){
        for(Material m : materials){
            addComponent(m);
        }
    }

}
