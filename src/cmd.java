package jed;

public class cmd {

    private static int currentLine = 0;

    /* parser: the command parser for jed, takes user input and interprets the command */
    public static int parser(String cmd) {
        int err = 0;
        if (isNum(cmd)) {
            currentLine = Integer.parseInt(cmd) - 1;
            System.out.printf("%d %s\n", currentLine + 1, jed.file.buffer.get(currentLine));
        } else {
            char c = cmd.charAt(0);
            switch (c) {
                case 'w':
                    if (cmd.length() > 1) {
                        String s[] = new String[2];
                        s = cmd.split(" ");
                        jed.file.hasName = true;
                        jed.file.fileName = s[1];
                    }
                    err = jed.file.writeFile();
                    break;
                case 'q': // q quits
                    return 1;
                case 'a': // a appends
                    append();
                    break;
                case 'p': // p prints lines
                    jed.file.printFile(false);
                    break;
                case 'n': //n prints lines with numbers
                    jed.file.printFile(true);
                    break;
                case 'c': // c change current line
                    changeLine();
                    break;
                case 'd':
                    deleteLine(); // d delete current line
                    break;
                case 'g': // g/expression/ finds user input
                    find(cmd.substring(2, cmd.length() - 1));
                    break;
                case '%': // %s/expression/newExpression/ replaces an expression with new user input through the whole file
                    String s[] = new String[3];
                    s = cmd.split("/");
                    replace(0, jed.file.buffer.size(), s[1], s[2]);
                    break;
                case '<': // <integer, integer>command works with d and s/ex/nex/
                    String t[] = new String[2];
                    t = cmd.split(">");
                    range(t[0], t[1]);
                    break;
                case 's': // s/expression/newExpression/ replaces an expression with new user input in the current line
                    String m[] = new String[3];
                    m = cmd.split("/");
                    replace(currentLine, currentLine + 1, m[1], m[2]);
                    break;
                case 'o':  // o fileName opens a file
                    jed.file.fileName = cmd.substring(2,cmd.length());
                    jed.file.hasName = true;
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

    /* range: gets the inputed range and runs the command with the range */
    private static void range(String range, String cmd) {
        String num[] = new String[2];
        num = range.replace("<", "").split(",");
        String temp[] = new String[3];
        temp = cmd.split("/");
        int start = Integer.parseInt(num[0]) - 1, stop = Integer.parseInt(num[1]);
        if (start <= 0 || start-1 >jed.file.buffer.size() || stop <= 0 || stop-1 > jed.file.buffer.size() || stop <= start)
            System.out.println("?");
        if (temp[0].equals("s"))
            replace(start, stop, temp[1], temp[2]);
        else if (temp[0].equals("d"))
            deleteLine(start, stop);
    }

    /* deleteLine: deletes line in the range of start to stop */
    private static void deleteLine(int start, int stop) {
        for (int i = start; i < stop;  i++) {
            jed.file.buffer.remove(start);
        }
    }

    /* replace: replaces an expression with new user input */
    private static void replace(int start, int stop, String ex, String newEx) {
        for (int i = start; i < stop; i++) {
            String line = jed.file.buffer.get(i);
            jed.file.buffer.add(i, line.replace(ex, newEx));
            jed.file.buffer.remove(i + 1);
        }
    }

    /* find: finds user input in the file and prints it with line numbers */
    private static void find(String expression) {
        for (int i = 0; i < jed.file.buffer.size(); i++) {
            String line = jed.file.buffer.get(i);
            for (int j = 0, k = expression.length(); k < line.length(); j++, k++) {
                if (line.substring(j, k).equals(expression)) {
                    System.out.printf("%d %s\n", i + 1, line);
                    break;
                }
            }
        }
    }

    /* changeLine: changes the current line */
    private static void changeLine() {
        jed.file.buffer.remove(currentLine);
        String input;
        int line = currentLine;
        while (!(input = jed.kbIn.nextLine()).equals("."))
            jed.file.buffer.add(line++, input);
    }

    /* deleteLine: deletes the current line */
    private static void deleteLine() {
        jed.file.buffer.remove(currentLine);
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

    /* append: appends user input to the end of a file */
    private static void append() {
        String input;
        while (!(input = jed.kbIn.nextLine()).equals("."))
            jed.file.buffer.add(input);
    }
}