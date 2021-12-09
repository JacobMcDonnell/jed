package jed;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class filebuffer {

    public ArrayList<String> buffer;
    public String fileName;
    public boolean hasName;

    public filebuffer(String fn, boolean hn) {
        buffer = new ArrayList<String>(); // Arraylist that stores every line of a file in strings
        hasName = hn;
        if (hasName)
            fileName = fn;
    }

    /* printFile: prints strings from file arraylist, if num is true it includes line numbers */
    public void printFile(boolean num) {
        for (int i = 0; i < buffer.size(); i++) {
            if (num)
                System.out.printf("%d %s\n", i+1, buffer.get(i));
            else
                System.out.println(buffer.get(i));
        }
    }

    /* createFile: creates a file, only to be called by writeFile */
    private int createFile() {
        try {
            File f = new File(fileName);
            if (f.createNewFile())
                return 1;
            return 0;
        } catch (IOException e) {
            return -1;
        }
    }

    /* writeFile: reads strings from file arraylist and prints them into a file */
    public int writeFile() {
        if (!hasName) {
            System.out.println("?");
            return 1;
        }
        int err = createFile();
        if (err == -1)
            return 1;
        try {
            FileWriter fw = new FileWriter(fileName);
            PrintWriter pw = new PrintWriter(fw);
            for (int i = 0; i < buffer.size(); i++)
                pw.println(buffer.get(i));
            pw.close();
        } catch (IOException e) {
            return 1;
        }
        return 0;
    }

    /* readFile: opens a file and stores each line in a string arraylist called file */
    public int readFile() {
        try {
            File fileObj = new File(fileName);
            Scanner fileReader = new Scanner(fileObj);
            while(fileReader.hasNextLine())
                buffer.add(fileReader.nextLine());
            fileReader.close();
        } catch (FileNotFoundException e) {
            createFile();
        }
        return 0;
    }
}