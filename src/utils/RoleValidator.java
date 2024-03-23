package utils;

import exceptions.UnauthorizedAccessException;
import models.users.Artist;
import models.users.Host;
import models.users.User;
import enums.UserRoleEnum;

public class RoleValidator {

    public static <T extends User> T validateRole(User user, UserRoleEnum requiredRole, Class<T> userClass) {
        if (user == null) {
            throw new UnauthorizedAccessException("User must be logged in.");
        }

        boolean isRoleValid = switch (requiredRole) {
            case ARTIST -> user instanceof Artist;
            case HOST -> user instanceof Host;
            case REGULAR -> true;
        };

        if (!isRoleValid) {
            throw new UnauthorizedAccessException("User does not have the required role: " + requiredRole);
        }

        return userClass.cast(user);
    }
}
