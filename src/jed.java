package jed;

import java.util.Scanner;

public class jed {

    private static Scanner kbIn = new Scanner(System.in);
    public static filebuffer file;

    public static void main(String args[]) {
        String fileName = null;
        boolean hasName = true;
        try { // checks if the user provided a file name
            fileName = args[0];
        } catch (IndexOutOfBoundsException e) {
            hasName = false;
        }

        file = new filebuffer(fileName, hasName);

        while (true) {
            String line = kbIn.nextLine();
            int err = parser(line);
            if (err == 1)
                break;
        }
        kbIn.close();
    }

    /* parser: the command parser for jed, takes user input and interprets the command */
    private static int parser(String cmd) {
        int err = 0;
        if (isNum(cmd)) {
            jed.file.changeCurrentLine(Integer.parseInt(cmd) - 1);
            System.out.printf("%d %s\n", jed.file.getCurrentLine() + 1, jed.file.getLine(jed.file.getCurrentLine()));
        } else {
            char c = cmd.charAt(0);
            switch (c) {
                case 'w':
                    if (cmd.length() > 1) {
                        String s[] = new String[2];
                        s = cmd.split(" ");
                        jed.file.changeName(s[1]);
                    }
                    err = jed.file.writeFile();
                    break;
                case 'q': // q quits
                    return 1;
                case 'a': // a appends
                    jed.file.append(kbIn);
                    break;
                case 'p': // p prints lines
                    jed.file.printFile(false);
                    break;
                case 'n': //n prints lines with numbers
                    jed.file.printFile(true);
                    break;
                case 'c': // c change current line
                    jed.file.changeLine(kbIn);
                    break;
                case 'd':
                    jed.file.deleteLine(); // d delete current line
                    break;
                case 'g': // g/expression/ finds user input
                    jed.file.find(cmd.substring(2, cmd.length() - 1));
                    break;
                case '%': // %s/expression/newExpression/ replaces an expression with new user input through the whole file
                    String s[] = new String[3];
                    s = cmd.split("/");
                    jed.file.replace(0, jed.file.getFileSize(), s[1], s[2]);
                    break;
                case '<': // <integer, integer>command works with d and s/ex/nex/
                    String t[] = new String[2];
                    t = cmd.split(">");
                    jed.file.range(t[0], t[1]);
                    break;
                case 's': // s/expression/newExpression/ replaces an expression with new user input in the current line
                    String m[] = new String[3];
                    m = cmd.split("/");
                    jed.file.replace(jed.file.getCurrentLine(), jed.file.getCurrentLine() + 1, m[1], m[2]);
                    break;
                case 'o':  // o fileName opens a file
                    jed.file.changeName(cmd.substring(2,cmd.length()));
                    jed.file.readFile();
                    break;
                default:
                    System.out.println("?");
                    break;
            }
        }
        if (err == 1) // error in writing the file
            System.out.println("Error writing the file");
        return 0;
    }

    /* isNum: checks if a string is a number, only to be used for changing lines */
    private static boolean isNum(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}