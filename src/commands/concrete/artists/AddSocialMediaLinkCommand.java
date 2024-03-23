package commands.concrete.artists;

import commands.Command;
import services.MusicService;
import services.UserService;

import static utils.InputUtils.askForField;

public class AddSocialMediaLinkCommand implements Command {
    @Override
    public void execute() {
        String platform = askForField("social media platform");
        String link = askForField("profile link");

        UserService.getInstance().addSocialMediaLinkToArtist(platform, link);
    }
}
