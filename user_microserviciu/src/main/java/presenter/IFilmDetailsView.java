package presenter;

public interface IFilmDetailsView {
    void setTitlu(String titlu);
    void setAnRealizare(String an);
    void setTipFilm(String tip);
    void setCategorieFilm(String categorie);
    void setDescriere(String descriere);
    void setRegizor(String numeComplet, String an, String nationalitate);
    void setScenarist(String numeComplet, String an, String nationalitate);
    void setActori(String[] numeActori);
    void setImagini(String[] caiImagini);
}