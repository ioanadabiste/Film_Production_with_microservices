package infrastracture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActorRepository
        extends JpaRepository<ActorEntity, Integer> {
    List<ActorEntity> findByNumeContainingIgnoreCase(String nume);
}