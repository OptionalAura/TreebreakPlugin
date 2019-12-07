package main.java.dp.plugin.pvp;

/*
    @author Daniel Allen
    6-Dec-2019
*/
public enum WeaponID {
    SWORD(0, "Sword"),
    BOW(1, "Bow"),
    RAILGUN(2, "Railgun"),
    FIRE_STAFF(3, "Wand"),
    BOOMER_TNT(4, "TNT");
    WeaponID(int id, String name){

    }
    private String name;
    private Integer id;
    public Integer getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
}

