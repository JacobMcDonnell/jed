package jed;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class filebuffer {

    private ArrayList<String> buffer;
    private String fileName;
    private boolean hasName;
    private int currentLine = 0;

    public filebuffer(String fn, boolean hn) {
        buffer = new ArrayList<String>(); // Arraylist that stores every line of a file in strings
        hasName = hn;
        if (hn) {
            fileName = fn;
            readFile();
        }
    }
    /* getFileSize: gets the size of the buffer arraylist */
    public int getFileSize() {
        return buffer.size();
    }

    /* getCurrentLine: returns the current line value */
    public int getCurrentLine() {
        return currentLine;
    }

    /* changeCurrentLine: changes the current line value */
    public void changeCurrentLine(int n) {
        currentLine = n;
        printLine(currentLine);
    }

    /* printLine: prints the line */
    private void printLine(int n) {
        System.out.printf("%d %s\n", n + 1, getLine(n));
    }

    /* getLine: returns the selected line as a string */
    public String getLine(int n) {
        return buffer.get(n);
    }

    /* changeName: changes/adds the file name */
    public void changeName(String name) {
        fileName = name;
        hasName = true;
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

    /* range: gets the inputed range and runs the command with the range */
    public void range(String range, String cmd) {
        String num[] = new String[2];
        num = range.replace("<", "").split(",");
        String temp[] = new String[3];
        temp = cmd.split("/");
        int start = Integer.parseInt(num[0]) - 1, stop = Integer.parseInt(num[1]);
        if (start <= 0 || start-1 > buffer.size() || stop <= 0 || stop-1 > buffer.size() || stop <= start)
            System.out.println("?");
        if (temp[0].equals("s"))
            replace(start, stop, temp[1], temp[2]);
        else if (temp[0].equals("d"))
            deleteLine(start, stop);
    }

    /* deleteLine: deletes line in the range of start to stop */
    public void deleteLine(int start, int stop) {
        for (int i = start; i < stop;  i++) {
            buffer.remove(start);
        }
    }

    /* replace: replaces an expression with new user input */
    public void replace(int start, int stop, String ex, String newEx) {
        for (int i = start; i < stop; i++) {
            String line = buffer.get(i);
            buffer.add(i, line.replace(ex, newEx));
            buffer.remove(i + 1);
        }
    }

    /* find: finds user input in the file and prints it with line numbers */
    public void find(String expression) {
        for (int i = 0; i < buffer.size(); i++) {
            String line = buffer.get(i);
            for (int j = 0, k = expression.length(); k < line.length(); j++, k++) {
                if (line.substring(j, k).equals(expression)) {
                    System.out.printf("%d %s\n", i + 1, line);
                    break;
                }
            }
        }
    }

    /* changeLine: changes the current line */
    public void changeLine(Scanner kbIn) {
        buffer.remove(currentLine);
        String input;
        int line = currentLine;
        while (!(input = kbIn.nextLine()).equals("."))
            buffer.add(line++, input);
    }

    /* deleteLine: deletes the current line */
    public void deleteLine() {
        buffer.remove(currentLine);
    }

    /* append: appends user input to the end of a file or after the current line */
    public void append(Scanner kbIn, boolean after) {
        String input;
        int line = currentLine + 1;
        while (!(input = kbIn.nextLine()).equals("."))
            if (after)
                buffer.add(line++, input);
            else
                buffer.add(input);
    }
}