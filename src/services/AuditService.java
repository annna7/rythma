package services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance = null;
    private static final String logFile = "logs/audit.csv";
    private AuditService() {}

    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void logAction(String action) {
        boolean isLoggedIn = UserService.getInstance().getCurrentUser(true) != null;
        int userId = isLoggedIn ? UserService.getInstance().getCurrentUser().getId() : -1;
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String log = String.format("%s,%s,%d\n", formattedTimestamp, action, userId);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(log);
        } catch (IOException e) {
            System.out.println("Failed to write to log file");
        }
    }

}
