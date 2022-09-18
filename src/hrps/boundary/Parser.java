package hrps.boundary;

import hrps.exception.InvalidDateTimeFormatException;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This class is used for getting and checking input from users.
 *
 * @author An Ruyi
 */
public class Parser {
    /**
     * Dummy constructor because all methods of this class are static.
     */
    private Parser(){}

    /**
     * Read an integer from standard input stream. If the input is not an integer or out of (int) range, print an error
     * message and return -1.
     * @return The input as an integer if it is a valid integer, -1 if the input is not an integer.
     */
    static int getChoice() {
        Scanner scanner = new Scanner(System.in);
        try {
            int result = scanner.nextInt();
            return result;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter an integer only.");
        }
        return -1;
    }

    /**
     * Read an integer from standard input stream. If the input is not an integer or out of (int) range, print an error
     * message and return -1.
     * @return The input as an integer if it is a valid integer, -1 if the input is not an integer.
     */
    static int getInt() {
        Scanner scanner = new Scanner(System.in);
        try {
            int result = scanner.nextInt();
            return result;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter an integer only.");
        }
        return -1;
    }

    /**
     * Read a double from standard input stream. If the input is not a double or is out of (double) range, print an
     * error message and return -1089454.
     * @return The input as a double if it is a valid double, -1089454 if the input is not a double.
     */
    static double getDouble() {
        Scanner scanner = new Scanner(System.in);
        try {
            double result = scanner.nextDouble();
            return result;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number only.");
        }
        return -1089454;
    }

    /**
     * Check if a String is in valid date input format.
     * @param dateInput The String to check.
     * @return true if the String matches the pattern "dddd-dd-dd dd:dd", where d is a decimal digit.
     */
    static boolean isValidInputDate(String dateInput) {
        if (!dateInput.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
            return false;
        }
        return true;
    }

}
