package presenter;

import model.repository.*;

public class MainPresenter {

    private final ActorRepository     actorRepo;
    private final RegizorRepository   regizorRepo;
    private final ScenaristRepository scenaristRepo;
    private final FilmPresenter       filmPresenter;

    public MainPresenter(FilmRepository filmRepo,
                         ActorRepository actorRepo,
                         RegizorRepository regizorRepo,
                         ScenaristRepository scenaristRepo) {
        this.actorRepo     = actorRepo;
        this.regizorRepo   = regizorRepo;
        this.scenaristRepo = scenaristRepo;
        this.filmPresenter = new FilmPresenter(filmRepo, actorRepo, regizorRepo, scenaristRepo);
    }

    public ActorRepository     getActorRepo()     { return actorRepo; }
    public RegizorRepository   getRegizorRepo()   { return regizorRepo; }
    public ScenaristRepository getScenaristRepo() { return scenaristRepo; }
    public FilmPresenter       getFilmPresenter() { return filmPresenter; }
}