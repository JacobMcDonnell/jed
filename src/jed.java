package jed;

import java.util.Scanner;

public class jed {

    public static Scanner kbIn = new Scanner(System.in);
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
            int err = cmd.parser(line);
            if (err == 1)
                break;
        }
        kbIn.close();
    }
}