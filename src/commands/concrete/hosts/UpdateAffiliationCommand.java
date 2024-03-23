package commands.concrete.hosts;

import commands.Command;
import models.users.Host;
import models.users.User;
import services.UserService;

import java.util.function.Predicate;

import static utils.InputUtils.askForField;

public class UpdateAffiliationCommand implements Command {
    @Override
    public void execute() {
        String affiliation = askForField("new affiliation");
        UserService.getInstance().updateHostAffiliation(affiliation);
    }

    @Override
    public Predicate<User> getVisibilityRule() {
        return user -> user instanceof Host;
    }

    @Override
    public String getCommandName() {
        return "Update affiliation";
    }

    @Override
    public String getCommandDescription() {
        return "Update the brand affiliation of the currently logged in podcast host";
    }
}
