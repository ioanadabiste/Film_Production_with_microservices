package domain.DAOContracts;

import domain.Film;
import java.util.List;
import java.util.Optional;

public interface IFilmDAO {
    List<Film> getAll();
    Optional<Film> getById(int id);
    List<Film> searchByTitle(String titlu);
    List<Film> filterByTip(String tip);
    List<Film> filterByCategorie(String categorie);
    List<Film> filterByAn(int an);
    boolean insert(Film f);
    Optional<Film> insertAndReturn(Film f);
    boolean update(Film f);
    boolean delete(int id);
}
