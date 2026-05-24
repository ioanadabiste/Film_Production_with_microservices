package infrastracture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FilmActorRepository
        extends JpaRepository<FilmActorEntity, Integer> {
    List<FilmActorEntity> findByIdFilm(int idFilm);
    List<FilmActorEntity> findByIdActor(int idActor);
}