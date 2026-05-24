package infrastracture;


import domain.FilmActor;
import jakarta.persistence.*;

@Entity
@Table(name = "film_actori")
public class FilmActorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_film", nullable = false)
    private int idFilm;

    @Column(name = "id_actor", nullable = false)
    private int idActor;

    @Column(length = 100)
    private String rol;

    public FilmActorEntity() {}

    public FilmActorEntity(FilmActor fa) {
        this.id = fa.getId();
        this.idFilm = fa.getIdFilm();
        this.idActor = fa.getIdActor();
        this.rol = fa.getRol();
    }

    public FilmActor toFilmActor() {
        FilmActor fa = new FilmActor();
        fa.setId(this.id);
        fa.setIdFilm(this.idFilm);
        fa.setIdActor(this.idActor);
        fa.setRol(this.rol);
        return fa;
    }

    public int getId() { return id; }
    public int getIdFilm() { return idFilm; }
    public int getIdActor() { return idActor; }
    public String getRol() { return rol; }

    public void setId(int id) { this.id = id; }
    public void setIdFilm(int idFilm) { this.idFilm = idFilm; }
    public void setIdActor(int idActor) { this.idActor = idActor; }
    public void setRol(String rol) { this.rol = rol; }
}