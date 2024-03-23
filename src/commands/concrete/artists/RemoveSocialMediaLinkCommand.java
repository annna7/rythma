package commands.concrete.artists;

import commands.Command;
import services.UserService;

import static utils.InputUtils.askForField;
public class RemoveSocialMediaLinkCommand implements Command {
    @Override
    public void execute() {
        String platform = askForField("social media platform");
        UserService.getInstance().removeSocialMediaLinkFromArtist(platform);
    }
}
