package infrastructure;

import domain.Scenarist;
import jakarta.persistence.*;

@Entity
@Table(name = "scenaristi")
public class ScenaristEntity {

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

    public ScenaristEntity() {}

    public ScenaristEntity(Scenarist s) {
        this.id = s.getId();
        this.nume = s.getNume();
        this.prenume = s.getPrenume();
        this.anNastere = s.getAnNastere();
        this.nationalitate = s.getNationalitate();
        this.foto = s.getFoto();
    }

    public Scenarist toScenarist() {
        Scenarist s = new Scenarist();
        s.setId(this.id);
        s.setNume(this.nume);
        s.setPrenume(this.prenume);
        s.setAnNastere(this.anNastere);
        s.setNationalitate(this.nationalitate);
        s.setFoto(this.foto);
        return s;
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