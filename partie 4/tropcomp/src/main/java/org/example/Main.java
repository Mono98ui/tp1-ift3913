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

    static int seuil = 0;

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
        seuil = Integer.parseInt(args[args.length - 1]);

        resursiveListDir(new File(pathFolder).listFiles());
        List<Node> arrCopy = new ArrayList<>(arr);
        Collections.sort(arrCopy, new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return o1.tloc - o2.tloc;
            }
        });

        List<Node> listTloc =  arrCopy.subList((arrCopy.size() * (100-seuil) / 100)-1, arrCopy.size()-1);

        Collections.sort(arr, new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.tcmp, o2.tcmp);
            }
        });

        List<Node> listTcmp = arr.subList((arr.size() * (100-seuil) / 100)-1, arr.size()-1);
        if (isCsv) {
            String CSVPath = csvName;
            File file = new File(CSVPath);
            CSVWriter writer = new CSVWriter(new FileWriter(CSVPath));

            for (int i = 0; i < listTloc.size() ; i++) {
                if (listTloc.contains(listTcmp.get(i))) {
                    System.out.println(listTloc.get(i).filepath);
                    File fileToRead = new File(listTloc.get(i).filepath);
                    writer.writeNext(listTloc.get(i).content);
                    writer.flush();
                    System.out.println("Données CSV insérées.");
                }
            }
        }else{
            for (int i = 0; i < listTloc.size() ; i++) {
                if (listTloc.contains(listTcmp.get(i))) {
                    System.out.println(listTloc.get(i).filepath);
                    File fileToRead = new File(listTloc.get(i).filepath);
                    System.out.println(Arrays.toString(listTloc.get(i).content));

                }
            }
        }
    }

    public static void resursiveListDir(File[] dir) throws IOException {
        for (File f :
                dir) {
            Pattern comp = Pattern.compile(".java$");
            if (comp.matcher(f.getName()).find()) {
                System.out.println(f.getAbsolutePath());
                System.out.println(f.isDirectory());
                System.out.println(f.getName());
                tls(f);
            }
            if (f.isDirectory()) {
                resursiveListDir(f.listFiles());
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
                System.out.println("Nom du paquet: " + packageName);
            }
        }
        scanner.close();

        //nom de la classe
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line, className = null;

        while ((line = br.readLine()) != null) {
            className = getClassName(line);
            if (className != null) {
                System.out.println("Class Name: " + className);
                break;
            }
        }

        //Alternative plus simple, mais pas nécessairement valide
        //String fileName = fileToRead.getName();

        //tloc de la classe
        int tlocOutput = tloc(fileToRead);
        System.out.println("tloc de la classe: " + tlocOutput);

        //tassert de la classe
        int tassertOutput = tassert(fileToRead);
        System.out.println("tassert de la classe: " + tassertOutput);

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