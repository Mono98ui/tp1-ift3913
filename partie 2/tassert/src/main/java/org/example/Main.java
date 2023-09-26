package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) throws IOException {
        File fileToRead = new File(args[0]);
        BufferedReader br = new BufferedReader(new FileReader(fileToRead));
        String line = br.readLine();

        Pattern pCom = Pattern.compile("^(assertArrayEquals|assertEquals|assertFalse" +
                "|assertNotEquals|assertNotNull|assertNotSame|assertNull|assertSame|" +
                "assertThat|assertThrows|assertTrue|fail)\\(.*\\);$");
        int nbrAssert = 0;

        while (line != null) {
            line = line.trim();
            if(pCom.matcher(line).find()){
                nbrAssert++;
            }
            line = br.readLine();
        }

        System.out.println(nbrAssert);
    }
}