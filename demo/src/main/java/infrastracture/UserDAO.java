package infrastracture;

import domain.DAOContracts.IUserDAO;
import domain.User;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

// adapter clasa
@Repository
public class UserDAO implements IUserDAO {
    private final UserRepository repository;

    public UserDAO(UserRepository repository) { this.repository = repository; }

    @Override
    public List<User> Users() {
        return repository.findAll().stream().map(UserEntity::toUser).collect(Collectors.toList());
    }

    @Override
    public User UserById(int id) {
        return repository.findById(id).map(UserEntity::toUser).orElse(null);
    }

    @Override
    public List<User> UsersByType(int userType) {
        return repository.findByUserType(userType).stream().map(UserEntity::toUser).collect(Collectors.toList());
    }

    @Override
    public boolean Insert(User user) {
        try { repository.save(new UserEntity(user)); return true; } catch (Exception e) { return false; }
    }

    @Override
    public boolean Update(User user) {
        try {
            int id = user.getId().getUserId();
            UserEntity existing = repository.findById(id).orElse(null);
            if (existing == null) return false;
            UserEntity entity = new UserEntity(user);
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                entity.setPassword(existing.getPassword());
            }
            repository.save(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean Delete(int id) {
        try { repository.deleteById(id); return true; } catch (Exception e) { return false; }
    }

    @Override
    public User Login(String email, String password) {
        return repository.findByEmailAndPassword(email, password)
                .map(UserEntity::toUser)
                .orElse(null);
    }
}