package domain;

public class FilmStatistics {
    private int id;
    private int idFilm;
    private String titluFilm;
    private int nrVizualizari;
    private double rating;

    public FilmStatistics() {}

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