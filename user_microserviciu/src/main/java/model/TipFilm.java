package model;

public enum TipFilm {
    ARTISTIC("Film Artistic"),
    SERIAL("Serial");

    private final String displayName;

    TipFilm(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}