package domain.DAOContracts;

import domain.User;
import java.util.List;

public interface IUserDAO {
    List<User> Users();
    User UserById(int id);
    List<User> UsersByType(int userType);
    boolean Insert(User user);
    boolean Update(User user);
    boolean Delete(int id);
    User Login(String email, String password);
}