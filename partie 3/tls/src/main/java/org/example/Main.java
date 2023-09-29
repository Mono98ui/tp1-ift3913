package org.example;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        File fileToRead = new File(args[0]);

        //chemin du fichier
        String filePath = fileToRead.getPath();

        Scanner scanner = new Scanner(fileToRead);
        String targetWord = "package";
        int lineNumber = 0;

        //nom du paquet
        while (scanner.hasNextLine()) {
            lineNumber++;
            String line = scanner.nextLine();
            if (line.contains(targetWord)) {
                int index = line.indexOf(targetWord);
                String packageName = line.substring(index + targetWord.length()).trim();
                System.out.println("Nom du paquet: " + packageName);
            }
        }

        scanner.close();

        //nom de la classe
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String className = getClassName(line);
                if (className != null) {
                    System.out.println("Class Name: " + className);
                    break; // Assuming there's only one class per file
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Alternative plus simple, mais pas nécessairement valide
        //String fileName = fileToRead.getName();

        //tloc de la classe
        int tlocOutput = tloc(fileToRead);
        System.out.println("tloc de la classe: " + tlocOutput);

        //tassert de la classe
        int tassertOutput = tassert(fileToRead);
        System.out.println("tassert de la classe: " +tassertOutput);

        //tcmp de la classe
        double tcmp = tlocOutput/tassertOutput;

    }

    static String getClassName(String line) {
        // Ignore les mentions de class dans les commentaires
        if (line.trim().startsWith("//")) {
            return null;
        }

        Pattern pattern = Pattern.compile("class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    static int tloc(File fileToRead) throws IOException {
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

    static int tassert(File fileToRead) throws IOException {
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


