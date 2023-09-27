package org.example;

import java.io.File;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        File dirName = new File(args[0]);
        File[] dir = dirName.listFiles();
        System.out.println(args[1]);
        for (File f:
             dir) {
            System.out.println(f.getAbsolutePath());
            System.out.println(f.isDirectory());
        }

    }
}