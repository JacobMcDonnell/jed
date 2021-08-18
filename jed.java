import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

class jed {

	private static ArrayList<String> file = new ArrayList<String>(); // Arraylist that stores every line of a file in strings
	private static Scanner kbIn = new Scanner(System.in);
	private static int currentLine = 0;
	private static boolean hasName = true;

	public static void main(String args[]) {
		String fileName = null;
		try { // checks if the user provided a file name
			fileName = args[0];
			readFile(args[0]);
		} catch (IndexOutOfBoundsException e) {
			hasName = false;
		}

		while (true) {
			String line = kbIn.nextLine();
			int err = parser(line, fileName);
			if (err == 1)
				break;
		}
		kbIn.close();
	}

	/* parser: the command parser for jed, takes user input and interprets the command */
	private static int parser(String cmd, String fileName) {
		int err = 0;
		if (cmd.equals("w")) // w writes the file
			err = writeFile(fileName);
		else if (cmd.equals("q")) // q quits
			return 1;
		else if (cmd.equals("a")) // a appends
			append();
		else if (cmd.equals("p")) // p prints lines
			printFile(false);
		else if (cmd.equals("n")) //n prints lines with numbers
			printFile(true);
		else if (isNum(cmd)) { // some positive integer to change lines
			currentLine = Integer.parseInt(cmd) - 1;
			System.out.printf("%d %s\n", currentLine + 1, file.get(currentLine));
		} else if (cmd.equals("c")) // c change current line
			changeLine();
		else if (cmd.equals("d")) // d delete current line
			deleteLine();
		else if (cmd.substring(0,2).equals("w ")) { // w FILENAME writes a file with user inputed name
			String s[] = new String[2];
			s = cmd.split(" ");
			hasName = true;
			err = writeFile(s[1]);
		} else if (cmd.substring(0,1).equals("g")) { // g/expression/ finds user input
			find(cmd.substring(2, cmd.length() - 1));
		} else if (cmd.substring(0,2).equals("%s")) { // %s/expression/newExpression/ replaces an expression with new user input through the whole file
			String temp[] = new String[3];
			temp = cmd.split("/");
			replace(0, file.size(), temp[1], temp[2]);
		} else if (cmd.substring(0,1).equals("<")) { // <integer, integer>command works with d and s/ex/nex/
			String temp[] = new String[2];
			temp = cmd.split(">");
			range(temp[0], temp[1]);
		} else if (cmd.substring(0,1).equals("s")) { // s/expression/newExpression/ replaces an expression with new user input in the current line
			String temp[] = new String[3];
			temp = cmd.split("/");
			replace(currentLine, currentLine + 1, temp[1], temp[2]);
		}  else if (cmd.substring(0,1).equals("o")) { // o fileName opens a file
			readFile(cmd.substring(2,cmd.length()));
		} else // unknown command
			System.out.println("?");
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
		if (start <= 0 || start-1 >file.size() || stop <= 0 || stop-1 > file.size() || stop <= start)
			System.out.println("?");
		if (temp[0].equals("s"))
			replace(start, stop, temp[1], temp[2]);
		else if (temp[0].equals("d"))
			deleteLine(start, stop);
	}

	/* deleteLine: deletes line in the range of start to stop */
	private static void deleteLine(int start, int stop) {
		for (int i = start; i < stop;  i++) {
			file.remove(start);
		}
	}

	/* replace: replaces an expression with new user input */
	private static void replace(int start, int stop, String ex, String newEx) {
		for (int i = start; i < stop; i++) {
			String line = file.get(i);
			file.add(i, line.replace(ex, newEx));
			file.remove(i + 1);
		}
	}

	/* find: finds user input in the file and prints it with line numbers */
	private static void find(String expression) {
		for (int i = 0; i < file.size(); i++) {
			String line = file.get(i);
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
		file.remove(currentLine);
		String input;
		int line = currentLine;
		while (!(input = kbIn.nextLine()).equals("."))
			file.add(line++, input);
	}

	/* deleteLine: deletes the current line */
	private static void deleteLine() {
		file.remove(currentLine);
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
		while (!(input = kbIn.nextLine()).equals("."))
			file.add(input);
	}

	/* printFile: prints strings from file arraylist, if num is true it includes line numbers */
	private static void printFile(boolean num) {
		for (int i = 0; i < file.size(); i++) {
			if (num)
				System.out.printf("%d %s\n", i+1, file.get(i));
			else
				System.out.println(file.get(i));
		}
	}

	/* createFile: creates a file, only to be called by writeFile */
	private static int createFile(String fileName) {
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
	private static int writeFile(String fileName) {
		if (!hasName) {
			System.out.println("?");
			return 1;
		}
		int err = createFile(fileName);
		if (err == -1)
			return 1;
		try {
			FileWriter fw = new FileWriter(fileName);
			PrintWriter pw = new PrintWriter(fw);
			for (int i = 0; i < file.size(); i++)
				pw.println(file.get(i));
			pw.close();
		} catch (IOException e) {
			return 1;
		}
		return 0;
	}

	/* readFile: opens a file and stores each line in a string arraylist called file */
	private static int readFile(String fileName) {
		try {
			File fileObj = new File(fileName);
			Scanner fileReader = new Scanner(fileObj);
			while(fileReader.hasNextLine())
				file.add(fileReader.nextLine());
			fileReader.close();
		} catch (FileNotFoundException e) {
			createFile(fileName);
		}
		return 0;
	}
}
