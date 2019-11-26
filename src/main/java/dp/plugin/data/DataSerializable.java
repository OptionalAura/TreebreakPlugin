package main.java.dp.plugin.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

/*
    @author Daniel Allen
    18-Aug-2019
 */
public class DataSerializable implements Serializable {

    public Map<UUID, Boolean> fastEnabled;
    public transient Map<UUID, ArrayList<Integer>> inventoryDoubleClicks;
    public Map<UUID, Map<String, Integer>> userSortingOptions;
    public Boolean debugMode;
    public Boolean memeMode;

    public static DataSerializable getData() {
        FileInputStream fis = null;
        try {
            Bukkit.getConsoleSender().sendMessage("§1Getting data for DP...");
            File f = new File(System.getProperty("user.dir") + "/plugins/dp");
            if (f.exists()) {
                fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);

                DataSerializable saved = (DataSerializable) ois.readObject();
                return saved;
            } else {
                DataSerializable data = new DataSerializable();
                data.debugMode = false;
                data.fastEnabled = new HashMap<>();
                data.inventoryDoubleClicks = new HashMap<>();
                data.memeMode = false;
                data.userSortingOptions = new HashMap<>();
                return data;
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(DataSerializable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(DataSerializable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new DataSerializable();
    }

    public void saveData() throws IOException {
        new File(System.getProperty("user.dir") + "\\plugins\\dp").mkdirs();

        for (Field f : DataSerializable.class.getFields()) {
            if (f.getModifiers() != Modifier.TRANSIENT) {
                FileOutputStream fos = new FileOutputStream(new File(System.getProperty("user.dir") + "/plugins/dp/"));
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(this);
        byte[] data = stream.toByteArray();
        //fos.write(data);
        //fos.close();
        oos.close();
        stream.close();
    }
}
