package jed;

import java.util.Scanner;

public class jed {

    private static Scanner kbIn = new Scanner(System.in);
    private static filebuffer file;

    public static void main(String args[]) {
        String fileName = null;
        boolean hasName = true;
        try { // checks if the user provided a file name
            fileName = args[0];
        } catch (IndexOutOfBoundsException e) {
            hasName = false;
        }

        file = new filebuffer(fileName, hasName);

        /* Main command prompt */
        while (true) {
            System.out.print("> ");
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
            file.changeCurrentLine(Integer.parseInt(cmd) - 1);
        } else {
            char c = cmd.charAt(0);
            switch (c) {
                case 'w':
                    if (cmd.length() > 1) {
                        String s[] = new String[2];
                        s = cmd.split(" ");
                        file.changeName(s[1]);
                    }
                    err = file.writeFile();
                    break;
                case 'q': // q quits
                    return 1;
                case 'a': // a appends
                    file.append(kbIn, false);
                    break;
                case 'A': // A appends after the current line
                    file.append(kbIn, true);
                    break;
                case 'p': // p prints lines
                    file.printFile(false);
                    break;
                case 'n': //n prints lines with numbers
                    file.printFile(true);
                    break;
                case 'c': // c change current line
                    file.changeLine(kbIn);
                    break;
                case 'd':
                    file.deleteLine(); // d delete current line
                    break;
                case 'g': // g/expression/ finds user input
                    file.find(cmd.substring(2, cmd.length() - 1));
                    break;
                case '%': // %s/expression/newExpression/ replaces an expression with new user input through the whole file
                    String s[] = new String[3];
                    s = cmd.split("/");
                    file.replace(0, file.getFileSize(), s[1], s[2]);
                    break;
                case '<': // <integer, integer> prefix works with d and s/ex/nex/
                    String t[] = new String[2];
                    t = cmd.split(">");
                    file.range(t[0], t[1]);
                    break;
                case 's': // s/expression/newExpression/ replaces an expression with new user input in the current line
                    String m[] = new String[3];
                    m = cmd.split("/");
                    file.replace(file.getCurrentLine(), file.getCurrentLine() + 1, m[1], m[2]);
                    break;
                case 'o':  // o fileName opens a file
                    file.changeName(cmd.substring(2,cmd.length()));
                    file.readFile();
                    break;
                case 'h': // help command
                    help();
                    break;
                case '^': case '-': // previous line command
                    if ((file.getCurrentLine() - 1) > -1)
                        file.changeCurrentLine((file.getCurrentLine() - 1));
                    else
                        System.out.println("?");
                    break;
                case '+': // Next line command
                    if ((file.getCurrentLine() + 1) <= file.getFileSize())
                        file.changeCurrentLine((file.getCurrentLine() + 1));
                    else
                        System.out.println("?");
                    break;
                case '$': // Last line command
                    file.changeCurrentLine(file.getFileSize() - 1);
                    break;
                case '.': // Prints current line
                    System.out.println(file.getLine(file.getCurrentLine()));
                    break;
                case 'U': // Uppercase entire line
                    file.upperCase();
                    break;
                case 'l': // Lowercase entire line
                    file.lowerCase();
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

    private static void help() {
        System.out.println("Jed commands:\nq: quits.\nw: writes the file.\nw: filename: writes with inputted name.\no: filename: opens the file.\na: appends user input to the end of the file.\nA: appends user input after the current line.\np: prints the file.\nn: prints the file with line numbers.\nc: deletes and changes the current line.\nd: deletes the current line.\nl: makes line lower case.\nU: makes line upper case.\n-: goes to previous line.\n+: goes to next line.\n$: goes to last line.\n.: prints current line.\nAny integer: changes to that line number.\ng/expression/: finds and prints the expression.\n%s/expression/newExpression/: replaces an expression with new user input through the whole file.\n<integer, integer>: prefix works with d, l, U, and s/ex/nex/ for a range of line.\ns/expression/newExpression/: replaces an expression with new user input in the current line.\nh: prints the commands and their description.");
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