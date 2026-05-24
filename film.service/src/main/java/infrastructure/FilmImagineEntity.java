package infrastructure;


import domain.FilmImagine;
import jakarta.persistence.*;

@Entity
@Table(name = "film_imagini")
public class FilmImagineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_film", nullable = false)
    private int idFilm;

    @Column(name = "cale_imagine", length = 500)
    private String caleImagine;

    public FilmImagineEntity() {}

    public FilmImagineEntity(FilmImagine fi) {
        this.id = fi.getId();
        this.idFilm = fi.getIdFilm();
        this.caleImagine = fi.getCaleImagine();
    }

    public FilmImagine toFilmImagine() {
        FilmImagine fi = new FilmImagine();
        fi.setId(this.id);
        fi.setIdFilm(this.idFilm);
        fi.setCaleImagine(this.caleImagine);
        return fi;
    }

    public int getId() { return id; }
    public int getIdFilm() { return idFilm; }
    public String getCaleImagine() { return caleImagine; }

    public void setId(int id) { this.id = id; }
    public void setIdFilm(int idFilm) { this.idFilm = idFilm; }
    public void setCaleImagine(String c) { this.caleImagine = c; }
}