package infrastracture;


import domain.Actor;
import jakarta.persistence.*;

@Entity
@Table(name = "actori")
public class ActorEntity {

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

    public ActorEntity() {}

    public ActorEntity(Actor a) {
        this.id = a.getId();
        this.nume = a.getNume();
        this.prenume = a.getPrenume();
        this.anNastere = a.getAnNastere();
        this.nationalitate = a.getNationalitate();
        this.foto = a.getFoto();
    }

    public Actor toActor() {
        Actor a = new Actor();
        a.setId(this.id);
        a.setNume(this.nume);
        a.setPrenume(this.prenume);
        a.setAnNastere(this.anNastere);
        a.setNationalitate(this.nationalitate);
        a.setFoto(this.foto);
        return a;
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