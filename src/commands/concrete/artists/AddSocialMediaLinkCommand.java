package commands.concrete.artists;

import commands.Command;
import services.MusicService;

import static utils.InputUtils.askForField;

public class AddSocialMediaLinkCommand implements Command {
    @Override
    public void execute() {
        String platform = askForField("the social media platform");
        String link = askForField("the link to your profile");

        MusicService.getInstance().addSocialMediaLinkToArtist(platform, link);
    }
}
