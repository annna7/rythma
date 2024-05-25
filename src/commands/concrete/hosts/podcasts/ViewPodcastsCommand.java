package commands.concrete.hosts.podcasts;

import commands.Command;
import models.users.Host;
import models.users.User;
import services.PodcastService;

import java.sql.SQLException;
import java.util.function.Predicate;

import static utils.OutputUtils.showCollectionMessage;

public class ViewPodcastsCommand implements Command {
    @Override
    public void execute() throws SQLException {
        showCollectionMessage("podcasts", PodcastService.getInstance().getPodcastsForCurrentUser());
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Host;
    }

    @Override
    public String getCommandName() {
        return "View podcasts";
    }

    @Override
    public String getCommandDescription() {
        return "View all podcasts for the currently logged in user";
    }
}
