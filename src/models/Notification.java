package models;

import enums.NotificationTypeEnum;
import java.time.LocalDateTime;

public class Notification implements Comparable<Notification> {
    private int id;
    private final int subscriptionId;
    private final NotificationTypeEnum type;
    private final String message;
    private final LocalDateTime timestamp;
    private boolean isRead;

    public Notification(int subscriptionId, NotificationTypeEnum type, String message) {
        this.subscriptionId = subscriptionId;
        this.type = type;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
    }

    @Override
    public int compareTo(Notification other) {
        if (this.isRead != other.isRead) {
            return this.isRead ? 1 : -1;
        }
        return this.message.compareTo(other.message);
    }

    public int getId () {
        return id;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public NotificationTypeEnum getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }


    @Override
    public String toString() {
        return (isRead ? "Notification" : "New notification") + " - " + type + ": " + message + " (" + timestamp + ")";
    }
}
