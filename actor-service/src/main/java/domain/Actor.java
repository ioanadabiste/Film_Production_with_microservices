package domain;

public class Actor {
    private int id;
    private String nume;
    private String prenume;
    private int anNastere;
    private String nationalitate;
    private String foto;

    public Actor() {}

    public int getId() { return id; }
    public String getNume() { return nume; }
    public String getPrenume() { return prenume; }
    public int getAnNastere() { return anNastere; }
    public String getNationalitate() { return nationalitate; }
    public String getFoto() { return foto; }
    public String getNumeComplet() { return prenume + " " + nume; }

    public void setId(int id) { this.id = id; }
    public void setNume(String n) { this.nume = n; }
    public void setPrenume(String p) { this.prenume = p; }
    public void setAnNastere(int an) { this.anNastere = an; }
    public void setNationalitate(String nat) { this.nationalitate = nat; }
    public void setFoto(String foto) { this.foto = foto; }
}