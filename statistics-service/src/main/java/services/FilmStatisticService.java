package services;
import domain.FilmStatistics;
import infrastracture.FilmStatisticEntity;
import infrastracture.FilmStatisticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmStatisticService {

    private final FilmStatisticRepository repo;

    public FilmStatisticService(FilmStatisticRepository repo) {
        this.repo = repo;
    }

    public List<FilmStatistics> getAll() {
        return repo.findAll().stream()
                .map(FilmStatisticEntity::toFilmStatistic)
                .collect(Collectors.toList());
    }

    public Optional<FilmStatistics> getById(int id) {
        return repo.findById(id).map(FilmStatisticEntity::toFilmStatistic);
    }

    public FilmStatistics getByFilmId(int idFilm) {
        FilmStatisticEntity e = repo.findByIdFilm(idFilm);
        return e != null ? e.toFilmStatistic() : null;
    }

    public List<FilmStatistics> getTopByRating() {
        return repo.findByOrderByRatingDesc().stream()
                .limit(5)
                .map(FilmStatisticEntity::toFilmStatistic)
                .collect(Collectors.toList());
    }

    public List<FilmStatistics> getTopByVizualizari() {
        return repo.findByOrderByNrVizualizariDesc().stream()
                .limit(5)
                .map(FilmStatisticEntity::toFilmStatistic)
                .collect(Collectors.toList());
    }

    public boolean insert(FilmStatistics fs) {
        try {
            repo.save(new FilmStatisticEntity(fs));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean update(FilmStatistics fs) {
        try {
            if (!repo.existsById(fs.getId())) return false;
            repo.save(new FilmStatisticEntity(fs));
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean delete(int id) {
        try {
            if (!repo.existsById(id)) return false;
            repo.deleteById(id);
            return true;
        } catch (Exception e) { return false; }
    }
}