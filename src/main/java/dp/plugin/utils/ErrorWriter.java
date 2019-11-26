package main.java.dp.plugin.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.bukkit.Bukkit;

/*
    @author Daniel Allen
    17-Aug-2019
 */
public class ErrorWriter {

    public static void printErrorToConsole(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        Bukkit.getConsoleSender().sendMessage("§c" + sw.toString());
    }
}