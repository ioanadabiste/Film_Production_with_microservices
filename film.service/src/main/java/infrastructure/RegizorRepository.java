package infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegizorRepository
        extends JpaRepository<RegizorEntity, Integer> {
}