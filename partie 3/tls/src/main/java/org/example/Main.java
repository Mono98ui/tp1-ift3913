package org.example;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVWriter;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static String csvName = "";
    static String pathFolder = "";
    static boolean isCsv = false;
    static ArrayList<Node> arr;

    static class Node {
        String filepath = "";
        int tloc = 0;
        double tcmp = 0;
        String[] content;

        public Node(String filepath, int tloc, double tcmp) {
            this.filepath = filepath;
            this.tloc = tloc;
            this.tcmp = tcmp;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args[0].contains("-o")) {
            csvName = args[1];
            pathFolder = args[2];
            isCsv = true;
        } else {
            pathFolder = args[0];
        }

        arr = new ArrayList<Node>();
        recursiveListDir(new File(pathFolder).listFiles());

        if (isCsv) {// Name of the CSV file
            String CSVPath = csvName;
            BufferedWriter writer = null;

            try {
                writer = new BufferedWriter(new FileWriter(CSVPath));

                for (int i = 0; i < arr.size(); i++) {
                    File fileToRead = new File(arr.get(i).filepath);
                    String rep =  Arrays.toString(arr.get(i).content).replace("[","")
                            .replace("]","")+"\n";
                    writer.write(rep);
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 0; i < arr.size(); i++) {
                System.out.println(Arrays.toString(arr.get(i).content));
            }
        }
    }

    public static void recursiveListDir(File[] dir) throws IOException {
        for (File f :
                dir) {
            Pattern comp = Pattern.compile(".java$");
            if (comp.matcher(f.getName()).find()) {
                tls(f);
            }
            if (f.isDirectory()) {
                recursiveListDir(f.listFiles());
            }
        }
    }

    static String getClassName(String line) {
        // Ignore les mentions de class dans les commentaires
        if (line.trim().startsWith("//")) {
            return null;
        }

        Pattern classPattern = Pattern.compile("class\\s+(\\w+)");
        Matcher matcher = classPattern.matcher(line);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    static int tloc(File fileToRead) throws IOException {
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

    static void tls(File fileToRead) throws IOException {


        //chemin du fichier
        String filePath = fileToRead.getPath();

        Scanner scanner = new Scanner(fileToRead);
        String targetWord = "package";
        int lineNumber = 0;

        //nom du paquet
        String packageName = null;

        while (scanner.hasNextLine()) {
            lineNumber++;
            String line = scanner.nextLine();
            if (line.contains(targetWord)) {
                int index = line.indexOf(targetWord);
                packageName = line.substring(index + targetWord.length()).trim();
            }
        }
        scanner.close();

        //nom de la classe
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line, className = null;

        while ((line = br.readLine()) != null) {
            className = getClassName(line);
            if (className != null) {
                break;
            }
        }

        //Alternative plus simple, mais pas nécessairement valide
        //String fileName = fileToRead.getName();

        //tloc de la classe
        int tlocOutput = tloc(fileToRead);
        //tassert de la classe
        int tassertOutput = tassert(fileToRead);
        //tcmp de la classe
        double tcmpOutput = 0;

        if (tlocOutput != 0 && tassertOutput != 0) {
            tcmpOutput = (double) (tlocOutput / tassertOutput);
        }

        String currentLine[] = {filePath, packageName, className, Integer.toString(tlocOutput),
                Integer.toString(tassertOutput), Double.toString(tcmpOutput)};
        Node newNode = new Node(fileToRead.getAbsolutePath(), tlocOutput, tcmpOutput);
        newNode.content = currentLine;
        arr.add(newNode);
    }
}