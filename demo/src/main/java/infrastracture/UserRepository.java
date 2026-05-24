package infrastracture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    java.util.List<UserEntity> findByUserType(int userType);
    Optional<UserEntity> findByEmailAndPassword(String email, String password);
}