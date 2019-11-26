package main.java.dp.plugin.backup;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;

/*
    @author Daniel Allen
    15-Aug-2019
 */
public class WorldWriter implements Runnable {

    World world;

    public WorldWriter(World w) {
        this.world = w;
    }

    public void makeBackup(World w) throws IOException {
        String curDir = System.getProperty("user.dir");
        File f = new File(curDir + "\\Backup\\" + world.getName() + ".world");
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(f);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(stream);
        out.writeObject(world);

        byte[] data = stream.toByteArray();
        fos.write(data);
    }

    @Override
    public void run() {
        try {
            makeBackup(world);
        } catch (IOException ex) {
            Bukkit.getConsoleSender().sendMessage("§cError backing up " + world.getName() + "! " + Arrays.toString(ex.getStackTrace()));
        }
    }
}
