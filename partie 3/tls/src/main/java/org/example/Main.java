package org.example;

import java.io.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        int tlocOutput = tloc(args);
        int tassertOutput = tassert(args);
        System.out.println(tlocOutput);
        System.out.println(tassertOutput);
    }

    static int tloc(String[] args) throws IOException {
        File fileToRead = new File(args[0]);
        //System.out.println(fileToRead.getAbsolutePath());
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
        return (nbrLine - nbrComment);
    }

    static int tassert(String[] args) throws IOException {
        File fileToRead = new File(args[0]);
        BufferedReader br = new BufferedReader(new FileReader(fileToRead));
        String line = br.readLine();

        Pattern pCom = Pattern.compile("^(assertArrayEquals|assertEquals|assertFalse" +
                "|assertNotEquals|assertNotNull|assertNotSame|assertNull|assertSame|" +
                "assertThat|assertThrows|assertTrue|fail)\\(.*\\);$");
        int nbrAssert = 0;

        while (line != null) {
            line = line.trim();
            if (pCom.matcher(line).find()) {
                nbrAssert++;
            }
            line = br.readLine();
        }

        return (nbrAssert);
    }


}


