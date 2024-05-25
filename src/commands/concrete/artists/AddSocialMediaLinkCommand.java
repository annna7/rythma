package commands.concrete.artists;

import commands.Command;
import models.users.Artist;
import models.users.User;
import services.UserService;

import java.sql.SQLException;
import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class AddSocialMediaLinkCommand implements Command {
    @Override
    public void execute() throws SQLException {
        String platform = askForField("social media platform");
        String link = askForField("profile link");

        UserService.getInstance().addSocialMediaLinkToArtist(platform, link);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Artist;
    }

    @Override
    public String getCommandName() {
        return "Add social media link";
    }

    @Override
    public String getCommandDescription() {
        return "Add (or update) a social media link, such as Instagram or YouTube, to the currently logged in artist";
    }
}
