package domain;


public class FilmImagine {
    private int id;
    private int idFilm;
    private String caleimagine;

    public FilmImagine() {}
    public FilmImagine(int id, int idFilm, String caleImagine) {
        this.id = id;
        this.idFilm = idFilm;
        this.caleimagine = caleImagine;
    }

    public int getId() { return id; }
    public int getIdFilm() { return idFilm; }
    public String getCaleImagine() { return caleimagine; }

    public void setId(int id) { this.id = id; }
    public void setIdFilm(int idFilm) { this.idFilm = idFilm; }
    public void setCaleImagine(String c) { this.caleimagine = c; }
}