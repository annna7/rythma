import database.DatabaseConnector;
import services.CliService;

public class Main {
    public static void main(String[] args) {
        DatabaseConnector.connect();
        CliService.getInstance().run();
        DatabaseConnector.disconnect();
    }
}