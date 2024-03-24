package utils;

import enums.SongGenreEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Function;

public class InputUtils {
    private final static Scanner scanner = new Scanner(System.in);
    public static String askForField(String fieldName) {
        System.out.println("Enter your " + fieldName + ": ");
        return scanner.nextLine();
    }

    public static <T> T askForField(String fieldName, Function<String, T> parser) {
        System.out.println("Enter your " + fieldName + ": ");
        String input = scanner.nextLine();
        return parser.apply(input);
    }

    public static LocalDate askForReleaseDate() {
        System.out.println("Default release date is today. Press enter to use it.");
        String release = askForField("the release date (yyyy-mm-dd)");
        if (release.isEmpty()) {
            return LocalDate.now();
        } else {
            return LocalDate.parse(release);
        }
    }

    public static ArrayList< String > askForGenres() {
        ArrayList< String  > genres = new ArrayList<>();
        System.out.println("Available genres:");
        for (SongGenreEnum genre : SongGenreEnum.values()) {
            System.out.println(genre.ordinal() + ". " + genre);
        }
        System.out.println("Enter the genre number or -1 to finish");
        int genreNumber;
        while ((genreNumber = Integer.parseInt(askForField("the genre number"))) != -1) {
            genres.add(SongGenreEnum.values()[genreNumber].toString());
        }
        return genres;
    }

    public static ArrayList< String > askForEpisodeGuests() {
        ArrayList< String  > guests = new ArrayList<>();
        System.out.println("Enter the guests of the episode or -1 to finish");
        String guest;
        while (!(guest = askForField("guest")).equals("-1")) {
            guests.add(guest);
        }
        return guests;
    }


    public static int askForRole() {
        while (true) {
            System.out.println("Enter your role: ");
            System.out.println("1. Regular User");
            System.out.println("2. Artist");
            System.out.println("3. Podcast Host");

            int role = scanner.nextInt();
            scanner.nextLine();

            if (role >= 1 && role <= 3) {
                return role;
            }
        }
    }

    public static String askForPasswordConfirmation() {
        String password;
        String confirmPassword;
        scanner.nextLine();

        do {
            System.out.println("Enter your password: ");
            password = scanner.nextLine().trim();
            System.out.println("Confirm your password: ");
            confirmPassword = scanner.nextLine().trim();
        } while (!password.equals(confirmPassword));

        return password;
    }
}
