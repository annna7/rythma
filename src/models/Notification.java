package models;
import enums.NotificationTypeEnum;

import java.time.LocalDateTime;

public class Notification implements Comparable<Notification>{
    private static int idCounter = 0;
    private final int id = idCounter++;
    private final NotificationTypeEnum type;
    private final LocalDateTime timestamp;
    private boolean isRead;
    private final String message;
    int priority;

    public Notification(NotificationTypeEnum type, String message) {
        this.type = type;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isRead = false;
        this.priority = type.getPriority();
    }

    // compareTo method is used to sort the notifications in the following order:
    // 1. Unread notifications come before read notifications
    // 2. Notifications are sorted by priority
    // 3. Notifications are sorted by timestamp
    @Override
    public int compareTo(Notification other) {
        if (this.isRead != other.isRead) {
            return this.isRead ? 1 : -1;
        }
        int priorityComparison = Integer.compare(this.priority, other.priority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        return this.timestamp.compareTo(other.timestamp);
    }

    public int getPriority() {
        return priority;
    }

    public NotificationTypeEnum getType() {
        return type;
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

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return (isRead ? "Notification" : "New notification") + " - " + type + ": " + message + " (" + timestamp + ")";
    }
}

