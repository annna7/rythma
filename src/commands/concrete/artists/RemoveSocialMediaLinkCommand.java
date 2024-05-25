package commands.concrete.artists;

import commands.Command;
import models.users.Artist;
import models.users.User;
import services.UserService;

import java.sql.SQLException;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;
public class RemoveSocialMediaLinkCommand implements Command {
    @Override
    public void execute() throws SQLException {
        String platform = askForField("social media platform");
        UserService.getInstance().removeSocialMediaLinkFromArtist(platform);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Artist;
    }

    @Override
    public String getCommandName() {
        return "Remove social media link";
    }

    @Override
    public String getCommandDescription() {
        return "Remove a social media link, such as Instagram or YouTube, from the currently logged in artist";
    }
}
