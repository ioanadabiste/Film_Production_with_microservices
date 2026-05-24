package infrastracture;

import domain.FilmStatistics;
import jakarta.persistence.*;

@Entity
@Table(name = "film_statistici")
public class FilmStatisticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_film", nullable = false)
    private int idFilm;

    @Column(name = "titlu_film", length = 200)
    private String titluFilm;

    @Column(name = "nr_vizualizari")
    private int nrVizualizari;

    @Column
    private double rating;

    public FilmStatisticEntity() {}

    public FilmStatisticEntity(FilmStatistics fs) {
        this.id = fs.getId();
        this.idFilm = fs.getIdFilm();
        this.titluFilm = fs.getTitluFilm();
        this.nrVizualizari = fs.getNrVizualizari();
        this.rating = fs.getRating();
    }

    public FilmStatistics toFilmStatistic() {
        FilmStatistics fs = new FilmStatistics();
        fs.setId(this.id);
        fs.setIdFilm(this.idFilm);
        fs.setTitluFilm(this.titluFilm);
        fs.setNrVizualizari(this.nrVizualizari);
        fs.setRating(this.rating);
        return fs;
    }

    public int getId() { return id; }
    public int getIdFilm() { return idFilm; }
    public String getTitluFilm() { return titluFilm; }
    public int getNrVizualizari() { return nrVizualizari; }
    public double getRating() { return rating; }

    public void setId(int id) { this.id = id; }
    public void setIdFilm(int idFilm) { this.idFilm = idFilm; }
    public void setTitluFilm(String t) { this.titluFilm = t; }
    public void setNrVizualizari(int n) { this.nrVizualizari = n; }
    public void setRating(double r) { this.rating = r; }
}