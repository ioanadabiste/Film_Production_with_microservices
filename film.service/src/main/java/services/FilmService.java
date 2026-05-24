package services;

import domain.Film;
import domain.DAOContracts.IFilmDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final IFilmDAO filmDAO;

    public FilmService(IFilmDAO filmDAO) {
        this.filmDAO = filmDAO;
    }

    public List<Film> getAll() { return filmDAO.getAll(); }
    public Optional<Film> getById(int id) { return filmDAO.getById(id); }
    public List<Film> searchByTitle(String titlu) { return filmDAO.searchByTitle(titlu); }
    public List<Film> filterByTip(String tip) { return filmDAO.filterByTip(tip); }
    public List<Film> filterByCategorie(String categorie) { return filmDAO.filterByCategorie(categorie); }
    public List<Film> filterByAn(int an) { return filmDAO.filterByAn(an); }
    public boolean insert(Film f) { return filmDAO.insert(f); }
    public Optional<Film> insertAndReturn(Film f) { return filmDAO.insertAndReturn(f); }
    public boolean update(Film f) { return filmDAO.update(f); }
    public boolean delete(int id) { return filmDAO.delete(id); }

    public String exportToCsv(List<Film> films) {
        StringBuilder sb = new StringBuilder("id,titlu,anRealizare,tipFilm,categorieFilm,descriere\n");
        for (Film f : films) {
            sb.append(f.getId()).append(",")
              .append(f.getTitlu()).append(",")
              .append(f.getAnRealizare()).append(",")
              .append(f.getTipFilm()).append(",")
              .append(f.getCategorieFilm()).append(",")
              .append(f.getDescriere() != null ? f.getDescriere() : "").append("\n");
        }
        return sb.toString();
    }

    public String exportToJson(List<Film> films) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < films.size(); i++) {
            Film f = films.get(i);
            sb.append("{\"id\":").append(f.getId())
              .append(",\"titlu\":\"").append(f.getTitlu()).append("\"")
              .append(",\"anRealizare\":").append(f.getAnRealizare())
              .append(",\"tipFilm\":\"").append(f.getTipFilm()).append("\"")
              .append(",\"categorieFilm\":\"").append(f.getCategorieFilm()).append("\"")
              .append("}");
            if (i < films.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public String exportToXml(List<Film> films) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<filme>\n");
        for (Film f : films) {
            sb.append("  <film>\n")
              .append("    <id>").append(f.getId()).append("</id>\n")
              .append("    <titlu>").append(f.getTitlu()).append("</titlu>\n")
              .append("    <anRealizare>").append(f.getAnRealizare()).append("</anRealizare>\n")
              .append("    <tipFilm>").append(f.getTipFilm()).append("</tipFilm>\n")
              .append("    <categorieFilm>").append(f.getCategorieFilm()).append("</categorieFilm>\n")
              .append("  </film>\n");
        }
        sb.append("</filme>");
        return sb.toString();
    }
}
