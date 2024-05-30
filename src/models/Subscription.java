package models;

public final class Subscription {
    private int subscriptionId;
    private int userId;
    private int entityId;
    private String entityType;

    public Subscription(int subscriptionId, int userId, int entityId, String entityType) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.entityId = entityId;
        this.entityType = entityType;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public int getUserId() {
        return userId;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subscriptionId +
                ", userId=" + userId +
                ", entityId=" + entityId +
                ", entityType='" + entityType + '\'' +
                '}';
    }
}
