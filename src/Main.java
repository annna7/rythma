import database.DatabaseConnector;
import services.CliService;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection dbConn = DatabaseConnector.connect()) {
            System.out.println("Conectarea la baze de data a reusit!");
            CliService.getInstance().run();
        } catch (Exception e) {
            System.out.printf("Eroare intampinata: %s%n", e.getMessage());
        }
    }
}