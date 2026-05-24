package domain;

public class Producator {
    private int id;
    private String nume;
    private String prenume;
    private int anNastere;
    private String nationalitate;
    private String foto;
    private String companie;

    public Producator() {}

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
