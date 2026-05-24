package domain;

public enum CategorieFilm {
    ACTIUNE("Acțiune"),
    COMEDIE("Comedie"),
    DRAMA("Dramă"),
    SF("Science Fiction"),
    HORROR("Horror"),
    THRILLER("Thriller"),
    ROMANTIC("Romantic"),
    AVENTURA("Aventură"),
    DOCUMENTAR("Documentar"),
    ANIMATIE("Animație");

    private final String displayName;
    CategorieFilm(String d) { this.displayName = d; }
    public String getDisplayName() { return displayName; }
}