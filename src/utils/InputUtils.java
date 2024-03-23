package utils;

import enums.SongGenreEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class InputUtils {
    private final static Scanner scanner = new Scanner(System.in);
    public static String askForField(String fieldName) {
        System.out.println("Enter your " + fieldName + ": ");
        return scanner.nextLine();
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
}
