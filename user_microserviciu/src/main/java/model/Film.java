package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Film implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String titlu;
    private int anRealizare;
    private TipFilm tipFilm;
    private CategorieFilm categorieFilm;
    private String regizorId;
    private String scenaristId;
    private List<String> actorIds;
    private List<String> caiImagini; // 1-3 imagini
    private String descriere;

    public Film() {
        this.id = UUID.randomUUID().toString();
        this.actorIds = new ArrayList<>();
        this.caiImagini = new ArrayList<>();
    }

    public Film(String titlu, int anRealizare, TipFilm tipFilm, CategorieFilm categorieFilm) {
        this.id = UUID.randomUUID().toString();
        this.titlu = titlu;
        this.anRealizare = anRealizare;
        this.tipFilm = tipFilm;
        this.categorieFilm = categorieFilm;
        this.actorIds = new ArrayList<>();
        this.caiImagini = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public int getAnRealizare() {
        return anRealizare;
    }

    public void setAnRealizare(int anRealizare) {
        this.anRealizare = anRealizare;
    }

    public TipFilm getTipFilm() {
        return tipFilm;
    }

    public void setTipFilm(TipFilm tipFilm) {
        this.tipFilm = tipFilm;
    }

    public CategorieFilm getCategorieFilm() {
        return categorieFilm;
    }

    public void setCategorieFilm(CategorieFilm categorieFilm) {
        this.categorieFilm = categorieFilm;
    }

    public String getRegizorId() {
        return regizorId;
    }

    public void setRegizorId(String regizorId) {
        this.regizorId = regizorId;
    }

    public String getScenaristId() {
        return scenaristId;
    }

    public void setScenaristId(String scenaristId) {
        this.scenaristId = scenaristId;
    }

    public List<String> getActorIds() {
        return actorIds;
    }

    public void setActorIds(List<String> actorIds) {
        this.actorIds = actorIds;
    }

    public void adaugaActor(String actorId) {
        if (!actorIds.contains(actorId)) {
            actorIds.add(actorId);
        }
    }

    public void stergeActor(String actorId) {
        actorIds.remove(actorId);
    }

    public List<String> getCaiImagini() {
        return caiImagini;
    }

    public void setCaiImagini(List<String> caiImagini) {
        this.caiImagini = caiImagini;
    }

    public void adaugaImagine(String caleImagine) {
        if (caiImagini.size() < 3) {
            caiImagini.add(caleImagine);
        }
    }

    public void stergeImagine(String caleImagine) {
        caiImagini.remove(caleImagine);
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public String toString() {
        return titlu + " (" + anRealizare + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Film film = (Film) obj;
        return id.equals(film.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}