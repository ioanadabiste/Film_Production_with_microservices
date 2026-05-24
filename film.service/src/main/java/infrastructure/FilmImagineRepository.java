package infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmImagineRepository
        extends JpaRepository<FilmImagineEntity, Integer> {
    List<FilmImagineEntity> findByIdFilm(int idFilm);
}