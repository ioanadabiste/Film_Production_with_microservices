package presenter;

import java.util.List;

public interface IFilmView {
    String getTitlu();
    String getAnRealizare();
    String getTipFilm();
    String getCategorieFilm();
    String getDescriere();
    String getRegizorId();
    String getScenaristId();
    List<String> getActorIdsSelectati();
    List<String> getCaiImagini();
    String getIdFilmSelectat();
    void setImaginiSelectate(String[] cai);

    String getFiltruTip();
    String getFiltruCategorie();
    String getFiltruAn();

    void afiseazaFilme(List<String[]> randuri);
    void populeazaRegizori(List<String[]> optiuni);
    void populeazaScenaristi(List<String[]> optiuni);
    void populeazaActori(List<String[]> optiuni);
    void afiseazaMesaj(String mesaj);
    void afiseazaEroare(String eroare);
    void curataFormular();

    // View deschide dialogul și îi pasează presenterul de detalii
    void deschideDetalii(FilmDetailsPresenter detailsPresenter);
}