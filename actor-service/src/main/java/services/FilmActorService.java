package services;

import domain.FilmActor;
import infrastracture.FilmActorEntity;
import infrastracture.FilmActorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmActorService {

    private final FilmActorRepository filmActorRepository;

    public FilmActorService(FilmActorRepository repo) {
        this.filmActorRepository = repo;
    }

    public List<FilmActor> getByFilmId(int idFilm) {
        return filmActorRepository.findByIdFilm(idFilm).stream()
                .map(FilmActorEntity::toFilmActor).collect(Collectors.toList());
    }

    public List<FilmActor> getByActorId(int idActor) {
        return filmActorRepository.findByIdActor(idActor).stream()
                .map(FilmActorEntity::toFilmActor).collect(Collectors.toList());
    }

    public boolean insert(FilmActor fa) {
        try {
            filmActorRepository.save(new FilmActorEntity(fa));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean delete(int id) {
        try {
            if (!filmActorRepository.existsById(id)) return false;
            filmActorRepository.deleteById(id);
            return true;
        } catch (Exception e) { return false; }
    }
}