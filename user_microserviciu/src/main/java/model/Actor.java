package model;

public class Actor extends Persoana {
    private static final long serialVersionUID = 1L;

    public Actor() {
        super();
    }

    public Actor(String nume, String prenume, int anNastere, String nationalitate) {
        super(nume, prenume, anNastere, nationalitate);
    }
}