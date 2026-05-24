package infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenaristRepository
        extends JpaRepository<ScenaristEntity, Integer> {
}