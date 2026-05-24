package services;

import domain.Film;
import domain.DAOContracts.IFilmDAO;

import java.util.List;
import java.util.Optional;

/**
 * Decorator Pattern – adauga logging automat la operatiile CRUD
 * fara sa modifice FilmService original.
 *
 * In FilmController schimbi o singura linie:
 *   this.filmService = new LoggingFilmService(filmDAO);
 * in loc de:
 *   this.filmService = new FilmService(filmDAO);
 */
public class LoggingFilmService extends FilmService {

    public LoggingFilmService(IFilmDAO filmDAO) {
        super(filmDAO);
    }

    @Override
    public boolean insert(Film f) {
        System.out.println("[LOG] INSERT film: titlu=" + f.getTitlu()
                + ", an=" + f.getAnRealizare()
                + ", regizorId=" + f.getRegizorId());
        boolean result = super.insert(f);
        System.out.println("[LOG] INSERT result: " + (result ? "SUCCESS" : "FAIL"));
        return result;
    }

    @Override
    public Optional<Film> insertAndReturn(Film f) {
        System.out.println("[LOG] INSERT film: titlu=" + f.getTitlu());
        Optional<Film> result = super.insertAndReturn(f);
        System.out.println("[LOG] INSERT result: " + (result.isPresent() ? "SUCCESS id=" + result.get().getId() : "FAIL"));
        return result;
    }

    @Override
    public boolean update(Film f) {
        System.out.println("[LOG] UPDATE film: id=" + f.getId()
                + ", titlu=" + f.getTitlu());
        boolean result = super.update(f);
        System.out.println("[LOG] UPDATE result: " + (result ? "SUCCESS" : "FAIL"));
        return result;
    }

    @Override
    public boolean delete(int id) {
        System.out.println("[LOG] DELETE film: id=" + id);
        boolean result = super.delete(id);
        System.out.println("[LOG] DELETE result: " + (result ? "SUCCESS" : "FAIL"));
        return result;
    }

    @Override
    public List<Film> getAll() {
        List<Film> result = super.getAll();
        System.out.println("[LOG] GET ALL filme: " + result.size() + " inregistrari");
        return result;
    }

    @Override
    public Optional<Film> getById(int id) {
        Optional<Film> result = super.getById(id);
        System.out.println("[LOG] GET BY ID film: id=" + id
                + " -> " + (result.isPresent() ? "gasit" : "negasit"));
        return result;
    }
}