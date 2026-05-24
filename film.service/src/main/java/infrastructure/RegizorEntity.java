package infrastructure;


import domain.Regizor;
import jakarta.persistence.*;

@Entity
@Table(name = "regizori")
public class RegizorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String nume;

    @Column(nullable = false, length = 50)
    private String prenume;

    @Column(name = "an_nastere")
    private int anNastere;

    @Column(length = 50)
    private String nationalitate;

    @Column(length = 500)
    private String foto;

    public RegizorEntity() {}

    public RegizorEntity(Regizor r) {
        this.id = r.getId();
        this.nume = r.getNume();
        this.prenume = r.getPrenume();
        this.anNastere = r.getAnNastere();
        this.nationalitate = r.getNationalitate();
        this.foto = r.getFoto();
    }

    public Regizor toRegizor() {
        Regizor r = new Regizor();
        r.setId(this.id);
        r.setNume(this.nume);
        r.setPrenume(this.prenume);
        r.setAnNastere(this.anNastere);
        r.setNationalitate(this.nationalitate);
        r.setFoto(this.foto);
        return r;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public int getAnNastere() { return anNastere; }
    public String getNationalitate() { return nationalitate; }
    public String getFoto() { return foto; }

    public void setId(int id) { this.id = id; }
    public void setNume(String n) { this.nume = n; }
    public void setPrenume(String p) { this.prenume = p; }
    public void setAnNastere(int an) { this.anNastere = an; }
    public void setNationalitate(String nat) { this.nationalitate = nat; }
    public void setFoto(String foto) { this.foto = foto; }
}