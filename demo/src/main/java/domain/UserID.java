package domain;

public class UserID {
    private int userId;

    public UserID() {
    }

    public UserID(int userId) {
        this.userId = userId;
    }

    public UserID(UserID userId) {
        this.userId = userId.getUserId();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}