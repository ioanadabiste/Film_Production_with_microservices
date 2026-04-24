package presenter;

import model.*;
import model.repository.*;

public class FilmDetailsPresenter {

    private final Film film;
    private final ActorRepository actorRepository;
    private final RegizorRepository regizorRepository;
    private final ScenaristRepository scenaristRepository;

    private IFilmDetailsView view;

    public FilmDetailsPresenter(Film film,
                                ActorRepository actorRepository,
                                RegizorRepository regizorRepository,
                                ScenaristRepository scenaristRepository) {
        this.film                = film;
        this.actorRepository     = actorRepository;
        this.regizorRepository   = regizorRepository;
        this.scenaristRepository = scenaristRepository;
    }

    public void setView(IFilmDetailsView view) {
        this.view = view;
        populeazaView();
    }

    private void populeazaView() {
        view.setTitlu(film.getTitlu());
        view.setAnRealizare(String.valueOf(film.getAnRealizare()));
        view.setTipFilm(film.getTipFilm() != null ? film.getTipFilm().toString() : "");
        view.setCategorieFilm(film.getCategorieFilm() != null ? film.getCategorieFilm().toString() : "");
        view.setDescriere(film.getDescriere() != null ? film.getDescriere() : "");

        if (film.getRegizorId() != null) {
            Regizor r = regizorRepository.getById(film.getRegizorId());
            if (r != null) view.setRegizor(r.getNumeComplet(),
                    String.valueOf(r.getAnNastere()), r.getNationalitate());
        }

        if (film.getScenaristId() != null) {
            Scenarist s = scenaristRepository.getById(film.getScenaristId());
            if (s != null) view.setScenarist(s.getNumeComplet(),
                    String.valueOf(s.getAnNastere()), s.getNationalitate());
        }

        String[] numeActori = film.getActorIds().stream()
                .map(actorId -> {
                    Actor a = actorRepository.getById(actorId);
                    return a != null ? a.getNumeComplet() + " (n. " +
                            a.getAnNastere() + ", " + a.getNationalitate() + ")" : "";
                })
                .toArray(String[]::new);
        view.setActori(numeActori);

        view.setImagini(film.getCaiImagini().toArray(new String[0]));
    }
}