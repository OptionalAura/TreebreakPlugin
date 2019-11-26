package main.java.dp.plugin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
    @author Daniel Allen
    15-Aug-2019
*/
public class YAMLHandler {
    public static Map getYAML(File f) throws FileNotFoundException, InvalidFileException, IOException{
        Map<String, YAML> yamlMap = new HashMap<>();
        if(!f.exists())
            throw new FileNotFoundException("Specified file does not exist!");
        if(!f.getName().split(".")[f.getName().split(".").length-1].equalsIgnoreCase("yml"))
            throw new InvalidFileException("File type unsupported. Make sure the target file is a YAML (.yml) file.");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String curLine;
        ArrayList<String> lines = new ArrayList<>();
        while((curLine = br.readLine()) != null){
            lines.add(curLine);
        }
        for(int i = 0; i < curLine.length(); i++){

        }
        return yamlMap;
    }
}
class YAML{
    int depth;
    YAML parent;
    ArrayList<YAML> children = new ArrayList<YAML>();
    String text;
    public YAML(int d, YAML p, YAML[] c, String t, String id){
        this.depth = d;
        this.parent = p;
        for(int i = 0; i < c.length; i++){
            this.children.add(c[i]);
        }
        this.text = t;
    }
}
class InvalidFileException extends Exception{
    public InvalidFileException(String message){
        super(message);
    }
    public InvalidFileException(){
        super();
    }
}