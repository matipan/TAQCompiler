import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class IOFileManager {
	public static Scanner IO(String input, String output) {
		try {
			System.setOut(new PrintStream(new File(output)));
			return new Scanner(new File(input));
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}
}
