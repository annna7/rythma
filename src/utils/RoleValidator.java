package utils;

import models.users.*;

public class RoleValidator {
    public static void validateArtist(User user) {
        if (!(user instanceof Artist)) {
            throw new IllegalArgumentException("You must be an artist to perform this action!");
        }
    }

    public static void validateHost(User user) {
        if (!(user instanceof Host)) {
            throw new IllegalArgumentException("You must be a podcast host to perform this action!");
        }
    }
}
