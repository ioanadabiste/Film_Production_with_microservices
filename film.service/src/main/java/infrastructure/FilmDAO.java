package infrastructure;

import domain.Film;
import domain.DAOContracts.IFilmDAO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FilmDAO implements IFilmDAO {

    private final FilmRepository repository;

    public FilmDAO(FilmRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Film> getAll() {
        return repository.findByOrderByTipFilmAscAnRealizareAsc().stream()
                .map(FilmEntity::toFilm).collect(Collectors.toList());
    }

    @Override
    public Optional<Film> getById(int id) {
        return repository.findById(id).map(FilmEntity::toFilm);
    }

    @Override
    public List<Film> searchByTitle(String titlu) {
        return repository.findByTitluContainingIgnoreCase(titlu).stream()
                .map(FilmEntity::toFilm).collect(Collectors.toList());
    }

    @Override
    public List<Film> filterByTip(String tip) {
        return repository.findByTipFilm(tip).stream()
                .map(FilmEntity::toFilm).collect(Collectors.toList());
    }

    @Override
    public List<Film> filterByCategorie(String categorie) {
        return repository.findByCategorieFilm(categorie).stream()
                .map(FilmEntity::toFilm).collect(Collectors.toList());
    }

    @Override
    public List<Film> filterByAn(int an) {
        return repository.findByAnRealizare(an).stream()
                .map(FilmEntity::toFilm).collect(Collectors.toList());
    }

    @Override
    public boolean insert(Film f) {
        return insertAndReturn(f).isPresent();
    }

    @Override
    public Optional<Film> insertAndReturn(Film f) {
        try {
            FilmEntity saved = repository.save(new FilmEntity(f));
            return Optional.of(saved.toFilm());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean update(Film f) {
        try {
            if (!repository.existsById(f.getId())) return false;
            repository.save(new FilmEntity(f)); return true;
        } catch (Exception e) { return false; }
    }

    @Override
    public boolean delete(int id) {
        try {
            if (!repository.existsById(id)) return false;
            repository.deleteById(id); return true;
        } catch (Exception e) { return false; }
    }
}
