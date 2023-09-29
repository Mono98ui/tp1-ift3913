package org.example;

import java.io.File;
import java.util.regex.Pattern;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {
        File dirName = new File(args[0]);
        File[] dir = dirName.listFiles();
        System.out.println(args[1]);
        int seuil = Integer.parseInt(args[1]);

        resursiveListDir(dir,seuil);

    }

    public static void resursiveListDir(File[] dir, int seuil){
        for (File f:
                dir) {
            Pattern comp = Pattern.compile(".java$");
            if(comp.matcher(f.getName()).find()){
                //code a ajouter
                System.out.println(f.getAbsolutePath());
                System.out.println(f.isDirectory());
                System.out.println(f.getName());
            }
            if(f.isDirectory()){
                resursiveListDir(f.listFiles(), seuil);
            }
        }
    }
}