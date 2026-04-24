package presenter;

import model.*;
import model.repository.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FilmPresenter {

    private final FilmRepository filmRepository;
    private final ActorRepository actorRepository;
    private final RegizorRepository regizorRepository;
    private final ScenaristRepository scenaristRepository;

    private IFilmView view;

    public FilmPresenter(FilmRepository filmRepository,
                         ActorRepository actorRepository,
                         RegizorRepository regizorRepository,
                         ScenaristRepository scenaristRepository) {
        this.filmRepository      = filmRepository;
        this.actorRepository     = actorRepository;
        this.regizorRepository   = regizorRepository;
        this.scenaristRepository = scenaristRepository;
    }

    public void setView(IFilmView view) {
        this.view = view;
    }

    public void alegeImagini() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new FileNameExtensionFilter(
                "Imagini (jpg, jpeg, png, gif)", "jpg", "jpeg", "png", "gif"));
        chooser.setDialogTitle("Selectați imagini pentru film");

        int rezultat = chooser.showOpenDialog(null);
        if (rezultat == JFileChooser.APPROVE_OPTION) {
            String[] cai = Arrays.stream(chooser.getSelectedFiles())
                    .map(File::getAbsolutePath)
                    .toArray(String[]::new);
            view.setImaginiSelectate(cai);
        }
    }

    public void adauga() {
        String titlu       = view.getTitlu();
        String anStr       = view.getAnRealizare();
        String tipStr      = view.getTipFilm();
        String catStr      = view.getCategorieFilm();
        String descriere   = view.getDescriere();
        String regizorId   = view.getRegizorId();
        String scenaristId = view.getScenaristId();
        List<String> actorIds = view.getActorIdsSelectati();
        List<String> imagini  = view.getCaiImagini();

        if (titlu == null || titlu.isEmpty()) {
            view.afiseazaEroare("Titlul este obligatoriu!");
            return;
        }
        try {
            int an = Integer.parseInt(anStr);
            TipFilm tip = TipFilm.valueOf(tipStr);
            CategorieFilm categorie = CategorieFilm.valueOf(catStr);

            Film film = new Film();
            film.setId(UUID.randomUUID().toString());
            film.setTitlu(titlu);
            film.setAnRealizare(an);
            film.setTipFilm(tip);
            film.setCategorieFilm(categorie);
            film.setDescriere(descriere);
            film.setRegizorId(regizorId);
            film.setScenaristId(scenaristId);
            film.setActorIds(actorIds != null ? actorIds : new ArrayList<>());
            film.setCaiImagini(imagini != null ? imagini : new ArrayList<>());

            filmRepository.adaugaFilm(film);
            incarcaToate();
            view.curataFormular();
            view.afiseazaMesaj("Film adăugat cu succes!");
        } catch (NumberFormatException e) {
            view.afiseazaEroare("Anul realizării invalid!");
        } catch (IllegalArgumentException e) {
            view.afiseazaEroare("Tip sau categorie invalidă!");
        }
    }

    public void actualizeaza() {
        String id = view.getIdFilmSelectat();
        if (id == null) { view.afiseazaEroare("Selectați un film din tabel!"); return; }

        try {
            Film film = filmRepository.getFilmById(id);
            if (film != null) {
                film.setTitlu(view.getTitlu());
                film.setAnRealizare(Integer.parseInt(view.getAnRealizare()));
                film.setTipFilm(TipFilm.valueOf(view.getTipFilm()));
                film.setCategorieFilm(CategorieFilm.valueOf(view.getCategorieFilm()));
                film.setDescriere(view.getDescriere());
                film.setRegizorId(view.getRegizorId());
                film.setScenaristId(view.getScenaristId());
                film.setActorIds(view.getActorIdsSelectati());
                film.setCaiImagini(view.getCaiImagini());
                filmRepository.actualizeazaFilm(film);
                incarcaToate();
                view.curataFormular();
                view.afiseazaMesaj("Film actualizat cu succes!");
            }
        } catch (NumberFormatException e) {
            view.afiseazaEroare("Anul realizării invalid!");
        }
    }

    public void sterge() {
        String id = view.getIdFilmSelectat();
        if (id == null) { view.afiseazaEroare("Selectați un film din tabel!"); return; }
        filmRepository.stergeFilm(id);
        incarcaToate();
        view.curataFormular();
        view.afiseazaMesaj("Film șters cu succes!");
    }

    public void filtreaza() {
        String tipStr = view.getFiltruTip();
        String catStr = view.getFiltruCategorie();
        String anStr  = view.getFiltruAn();

        TipFilm tip = (tipStr == null || tipStr.isEmpty()) ? null : TipFilm.valueOf(tipStr);
        CategorieFilm cat = (catStr == null || catStr.isEmpty()) ? null : CategorieFilm.valueOf(catStr);
        Integer an = (anStr == null || anStr.isEmpty()) ? null : Integer.parseInt(anStr);

        List<Film> filme = filmRepository.filtreazaFilme(tip, cat, an);
        view.afiseazaFilme(toRanduri(filme));
    }

    public void incarcaToate() {
        view.afiseazaFilme(toRanduri(filmRepository.getFilme()));

        view.populeazaRegizori(
                regizorRepository.getAll().stream()
                        .map(r -> new String[]{r.getId(), r.getNumeComplet()})
                        .collect(Collectors.toList()));

        view.populeazaScenaristi(
                scenaristRepository.getAll().stream()
                        .map(s -> new String[]{s.getId(), s.getNumeComplet()})
                        .collect(Collectors.toList()));

        view.populeazaActori(
                actorRepository.getAll().stream()
                        .map(a -> new String[]{a.getId(), a.getNumeComplet()})
                        .collect(Collectors.toList()));
    }

    public void veziDetalii() {
        String id = view.getIdFilmSelectat();
        if (id == null) { view.afiseazaEroare("Selectați un film din tabel!"); return; }

        Film film = filmRepository.getFilmById(id);
        if (film == null) return;

        FilmDetailsPresenter detailsPresenter = new FilmDetailsPresenter(
                film, actorRepository, regizorRepository, scenaristRepository);

        view.deschideDetalii(detailsPresenter);
    }

    private List<String[]> toRanduri(List<Film> filme) {
        return filme.stream()
                .map(f -> new String[]{
                        f.getId(),
                        f.getTitlu(),
                        String.valueOf(f.getAnRealizare()),
                        f.getTipFilm() != null ? f.getTipFilm().toString() : "",
                        f.getCategorieFilm() != null ? f.getCategorieFilm().toString() : ""
                })
                .collect(Collectors.toList());
    }
}