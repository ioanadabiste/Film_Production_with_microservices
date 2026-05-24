package infrastracture;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmStatisticRepository
        extends JpaRepository<FilmStatisticEntity, Integer> {
    List<FilmStatisticEntity> findByOrderByRatingDesc();
    List<FilmStatisticEntity> findByOrderByNrVizualizariDesc();
    FilmStatisticEntity findByIdFilm(int idFilm);
}