package factories;

import models.users.*;

public class UserFactory {
    public static User createUser(int role, String username, String firstName, String lastName, String password, String additionalInfo) {
        return switch (role) {
            case 1 -> new User(username, firstName, lastName, password);
            case 2 -> new Artist(username, firstName, lastName, password, additionalInfo);
            case 3 -> new Host(username, firstName, lastName, password, additionalInfo);
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
