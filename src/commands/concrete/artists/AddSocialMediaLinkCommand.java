package commands.concrete.artists;

import commands.Command;
import services.MusicService;

import static utils.InputUtils.askForField;

public class AddSocialMediaLinkCommand implements Command {
    @Override
    public void execute() {
        String platform = askForField("social media platform");
        String link = askForField("profile link");

        MusicService.getInstance().addSocialMediaLinkToArtist(platform, link);
    }
}
