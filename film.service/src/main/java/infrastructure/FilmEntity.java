package infrastructure;

import domain.CategorieFilm;
import domain.Film;
import domain.TipFilm;
import jakarta.persistence.*;

@Entity
@Table(name = "filme")
public class FilmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String titlu;

    @Column(name = "an_realizare")
    private int anRealizare;

    @Enumerated(EnumType.STRING)
    @Column(name = "tip_film", length = 50)
    private TipFilm tipFilm;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie_film", length = 50)
    private CategorieFilm categorieFilm;

    @Column(columnDefinition = "TEXT")
    private String descriere;

    @Column(name = "regizor_id")
    private int regizorId;

    @Column(name = "scenarist_id")
    private int scenaristId;

    @Column(name = "producator_id")
    private int producatorId;

    public FilmEntity() {}

    public FilmEntity(Film f) {
        this.id = f.getId();
        this.titlu = f.getTitlu();
        this.anRealizare = f.getAnRealizare();
        this.tipFilm = f.getTipFilm();
        this.categorieFilm = f.getCategorieFilm();
        this.descriere = f.getDescriere();
        this.regizorId = f.getRegizorId();
        this.scenaristId = f.getScenaristId();
        this.producatorId = f.getProducatorId();
    }

    public Film toFilm() {
        Film f = new Film();
        f.setId(this.id);
        f.setTitlu(this.titlu);
        f.setAnRealizare(this.anRealizare);
        f.setTipFilm(this.tipFilm);
        f.setCategorieFilm(this.categorieFilm);
        f.setDescriere(this.descriere);
        f.setRegizorId(this.regizorId);
        f.setScenaristId(this.scenaristId);
        f.setProducatorId(this.producatorId);
        return f;
    }

    public int getId() { return id; }
    public String getTitlu() { return titlu; }
    public int getAnRealizare() { return anRealizare; }
    public TipFilm getTipFilm() { return tipFilm; }
    public CategorieFilm getCategorieFilm() { return categorieFilm; }
    public String getDescriere() { return descriere; }
    public int getRegizorId() { return regizorId; }
    public int getScenaristId() { return scenaristId; }
    public int getProducatorId() { return producatorId; }

    public void setId(int id) { this.id = id; }
    public void setTitlu(String t) { this.titlu = t; }
    public void setAnRealizare(int an) { this.anRealizare = an; }
    public void setTipFilm(TipFilm tip) { this.tipFilm = tip; }
    public void setCategorieFilm(CategorieFilm cat) { this.categorieFilm = cat; }
    public void setDescriere(String d) { this.descriere = d; }
    public void setRegizorId(int id) { this.regizorId = id; }
    public void setScenaristId(int id) { this.scenaristId = id; }
    public void setProducatorId(int id) { this.producatorId = id; }
}