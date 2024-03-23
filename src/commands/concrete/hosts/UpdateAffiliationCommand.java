package commands.concrete.hosts;

import commands.Command;
import services.UserService;

import static utils.InputUtils.askForField;

public class UpdateAffiliationCommand implements Command {
    @Override
    public void execute() {
        String affiliation = askForField("new affiliation");
        UserService.getInstance().updateHostAffiliation(affiliation);
    }
}
