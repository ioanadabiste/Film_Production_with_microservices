package domain;

public class User {
    private UserID id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private int userType;

    public User() {
    }

    public User(UserID id, String name, String surname,
                String email, int userType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.userType = userType;
    }

    public User(User user) {
        this.id = user.id;
        this.name = user.name;
        this.surname = user.surname;
        this.email = user.email;
        this.phone = user.phone;
        this.userType = user.userType;
    }

    public UserID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public int getUserType() {
        return userType;
    }

    public void setId(UserID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}