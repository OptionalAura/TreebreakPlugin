/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.treebreaker.plugin.utils;

/**
 *
 * @author dsato
 */
public class Version {
    private final int[] ids;
    public Version(String version){
        this(version, ".");
    }
    public Version(String version, String delimiter){
        String[] split = version.split(delimiter);
        ids = new int[Math.max(split.length, 1)];
        for(int i = 0; i < split.length; i++){
            String cur = split[i].replaceAll("[^0-9]", "");
            int val;
            if(cur.length() == 0){
                val = 0;
            } else {
                val = Integer.parseInt(cur);
            }
            ids[i] = val;
        }
    }
    public int[] getVersionNumber(){
        return ids;
    }
    public String getVersionString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ids.length - 1; i++){
            sb.append(ids[i]).append('.');
        }
        sb.append(ids[ids.length-1]);
        return sb.toString();
    }
    /**
     * <code>a.compareTo(b)</code>
     * @param other
     * @return -1 if <code>a</code> is less than <code>b</code><br>
     * 0 if <code>a</code> is equal to <code>b</code> <br>
     * 1 if <code>a</code> is greater than <code>b</code>
     */
    public int compareTo(Version other){
        for(int i = 0; i < Math.min(ids.length, other.ids.length); i++){
            if(ids[i] < other.ids[i]){
                return -1;
            }
            if(ids[i] > other.ids[i]){
                return 1;
            }
        }
        if(ids.length != other.ids.length){
            //TODO Fix this so 2.3 is equal to 2.3.0.0.0.0
            return ids.length > other.ids.length ? 1 : -1;
        }
        return 0;
    }
}
