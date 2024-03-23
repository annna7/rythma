package utils;

import java.util.Scanner;

public class InputUtils {
    private static Scanner scanner = new Scanner(System.in);
    public static String askForField(String fieldName) {
        System.out.println("Enter your " + fieldName + ": ");
        return scanner.nextLine();
    }
}
