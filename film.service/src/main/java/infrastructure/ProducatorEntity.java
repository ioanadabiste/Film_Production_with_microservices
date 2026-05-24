package infrastructure;

import domain.Producator;
import jakarta.persistence.*;

@Entity
@Table(name = "producatori")
public class ProducatorEntity {

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

    @Column(length = 100)
    private String companie;

    public ProducatorEntity() {}

    public ProducatorEntity(Producator p) {
        this.id = p.getId();
        this.nume = p.getNume();
        this.prenume = p.getPrenume();
        this.anNastere = p.getAnNastere();
        this.nationalitate = p.getNationalitate();
        this.foto = p.getFoto();
        this.companie = p.getCompanie();
    }

    public Producator toProducator() {
        Producator p = new Producator();
        p.setId(this.id);
        p.setNume(this.nume);
        p.setPrenume(this.prenume);
        p.setAnNastere(this.anNastere);
        p.setNationalitate(this.nationalitate);
        p.setFoto(this.foto);
        p.setCompanie(this.companie);
        return p;
    }

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public int getAnNastere() { return anNastere; }
    public String getNationalitate() { return nationalitate; }
    public String getFoto() { return foto; }
    public String getCompanie() { return companie; }

    public void setId(int id) { this.id = id; }
    public void setNume(String nume) { this.nume = nume; }
    public void setPrenume(String prenume) { this.prenume = prenume; }
    public void setAnNastere(int an) { this.anNastere = an; }
    public void setNationalitate(String nat) { this.nationalitate = nat; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setCompanie(String companie) { this.companie = companie; }
}
