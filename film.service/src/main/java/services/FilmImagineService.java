package service;
import domain.FilmImagine;
import infrastructure.FilmImagineEntity;
import infrastructure.FilmImagineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmImagineService {

    private final FilmImagineRepository filmImagineRepository;

    public FilmImagineService(FilmImagineRepository repo) {
        this.filmImagineRepository = repo;
    }

    public List<FilmImagine> getByFilmId(int idFilm) {
        return filmImagineRepository.findByIdFilm(idFilm).stream()
                .map(FilmImagineEntity::toFilmImagine)
                .collect(Collectors.toList());
    }

    public boolean insert(FilmImagine fi) {
        try {
            filmImagineRepository.save(new FilmImagineEntity(fi));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean delete(int id) {
        try {
            if (!filmImagineRepository.existsById(id)) return false;
            filmImagineRepository.deleteById(id);
            return true;
        } catch (Exception e) { return false; }
    }
}