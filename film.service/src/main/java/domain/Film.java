package domain;


import java.util.List;

public class Film {
    private int id;
    private String titlu;
    private int anRealizare;
    private TipFilm tipFilm;
    private CategorieFilm categorieFilm;
    private String descriere;
    private int regizorId;
    private int scenaristId;
    private int producatorId;
    private List<Integer> actorIds;

    public Film() {}

    public int getId() { return id; }
    public String getTitlu() { return titlu; }
    public int getAnRealizare() { return anRealizare; }
    public TipFilm getTipFilm() { return tipFilm; }
    public CategorieFilm getCategorieFilm() { return categorieFilm; }
    public String getDescriere() { return descriere; }
    public int getRegizorId() { return regizorId; }
    public int getScenaristId() { return scenaristId; }
    public int getProducatorId() { return producatorId; }
    public List<Integer> getActorIds() { return actorIds; }

    public void setId(int id) { this.id = id; }
    public void setTitlu(String t) { this.titlu = t; }
    public void setAnRealizare(int an) { this.anRealizare = an; }
    public void setTipFilm(TipFilm tip) { this.tipFilm = tip; }
    public void setCategorieFilm(CategorieFilm cat) { this.categorieFilm = cat; }
    public void setDescriere(String d) { this.descriere = d; }
    public void setRegizorId(int id) { this.regizorId = id; }
    public void setScenaristId(int id) { this.scenaristId = id; }
    public void setProducatorId(int id) { this.producatorId = id; }
    public void setActorIds(List<Integer> ids) { this.actorIds = ids; }
}