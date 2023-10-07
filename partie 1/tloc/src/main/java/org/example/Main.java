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

        Pattern pCom1 = Pattern.compile("^//");
        Pattern pCom2 = Pattern.compile("^(/\\*\\*|/\\*)");
        Pattern pCom3 = Pattern.compile("\\*/$");
        boolean isComment = false;
        int nbrComment = 0;
        int nbrLine = 0;

        while (line != null) {
            line = line.trim();

            if (pCom1.matcher(line).find()) {
                nbrComment++;
            } else if (pCom2.matcher(line).find()) {
                isComment = true;
                nbrComment++;
            } else if (pCom3.matcher(line).find()) {
                isComment = false;
                nbrComment++;
            } else if (isComment) {
                nbrComment++;
            }

            line = br.readLine();
            nbrLine++;
        }
        System.out.println(nbrLine - nbrComment);

    }
}