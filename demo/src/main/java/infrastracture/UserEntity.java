package infrastracture;

import domain.User;
import domain.UserID;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String surname;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private int userType;

    public UserEntity() {
    }

    public UserEntity(User user) {
        if (user.getId() != null) {
            this.id = user.getId().getUserId();
        }
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.userType = user.getUserType();
    }

    public User toUser() {
        User user = new User();
        user.setId(new UserID(this.id));
        user.setName(this.name);
        user.setSurname(this.surname);
        user.setEmail(this.email);
        user.setPhone(this.phone);
        user.setUserType(this.userType);
        // password NOT returned to frontend
        return user;
    }

    public int getId() {
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

    public void setId(int id) {
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