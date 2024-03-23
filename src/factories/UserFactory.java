package factories;

import enums.UserRoleEnum;
import models.users.*;

public class UserFactory {
    public static User createUser(UserRoleEnum role, String username, String firstName, String lastName, String password, String additionalInfo) {
        return switch (role) {
            case REGULAR -> new User(username, firstName, lastName, password);
            case ARTIST -> new Artist(username, firstName, lastName, password, additionalInfo);
            case HOST -> new Host(username, firstName, lastName, password, additionalInfo);
        };
    }
}
