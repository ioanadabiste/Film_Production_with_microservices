package domain;

public class Notification {
    private NotificationType type;
    private int userId;
    private String message;

    public Notification() {
    }

    public Notification(NotificationType type, int userId, String message) {
        this.type = type;
        this.userId = userId;
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}