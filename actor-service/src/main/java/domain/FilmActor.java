package domain;

public class FilmActor {
    private int id;
    private int idFilm;
    private int idActor;
    private String rol;

    public FilmActor() {}

    public int getId() { return id; }
    public int getIdFilm() { return idFilm; }
    public int getIdActor() { return idActor; }
    public String getRol() { return rol; }

    public void setId(int id) { this.id = id; }
    public void setIdFilm(int idFilm) { this.idFilm = idFilm; }
    public void setIdActor(int idActor) { this.idActor = idActor; }
    public void setRol(String rol) { this.rol = rol; }
}