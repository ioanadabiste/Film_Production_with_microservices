package domain;

public enum UserType {
    ANGAJAT(0), MANAGER(1), ADMINISTRATOR(2);

    private final int value;

    UserType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}