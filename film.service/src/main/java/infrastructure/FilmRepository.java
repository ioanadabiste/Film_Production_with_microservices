package infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmRepository
        extends JpaRepository<FilmEntity, Integer> {
    List<FilmEntity> findByTipFilm(String tipFilm);
    List<FilmEntity> findByCategorieFilm(String categorieFilm);
    List<FilmEntity> findByAnRealizare(int an);
    List<FilmEntity> findByTitluContainingIgnoreCase(String titlu);
    List<FilmEntity> findByOrderByTipFilmAscAnRealizareAsc();
}