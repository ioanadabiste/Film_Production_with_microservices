package infrastracture;

import domain.Actor;
import domain.FilmActor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ActorRepository actorRepository;
    private final FilmActorRepository filmActorRepository;

    public DataInitializer(ActorRepository actorRepository, FilmActorRepository filmActorRepository) {
        this.actorRepository = actorRepository;
        this.filmActorRepository = filmActorRepository;
    }

    @Override
    public void run(String... args) {
        if (actorRepository.count() > 0) return;

        ActorEntity a1 = saveActor("DiCaprio", "Leonardo", 1974, "American",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Leonardo_Dicaprio_Cannes_2019.jpg/220px-Leonardo_Dicaprio_Cannes_2019.jpg");
        ActorEntity a2 = saveActor("Marinescu", "Ana", 1990, "Română", null);
        ActorEntity a3 = saveActor("Hanks", "Tom", 1956, "American", null);

        saveFilmActor(1, a1.getId(), "Cobb");
        saveFilmActor(1, a3.getId(), "Saito");
        saveFilmActor(2, a2.getId(), "Maria");
        saveFilmActor(3, a2.getId(), "Detectiv Elena");
        saveFilmActor(3, a3.getId(), "Comisar Popa");

        System.out.println("[ActorService] Date demo initializate.");
    }

    private ActorEntity saveActor(String nume, String prenume, int an, String nat, String foto) {
        Actor a = new Actor();
        a.setNume(nume);
        a.setPrenume(prenume);
        a.setAnNastere(an);
        a.setNationalitate(nat);
        a.setFoto(foto);
        return actorRepository.save(new ActorEntity(a));
    }

    private void saveFilmActor(int idFilm, int idActor, String rol) {
        FilmActor fa = new FilmActor();
        fa.setIdFilm(idFilm);
        fa.setIdActor(idActor);
        fa.setRol(rol);
        filmActorRepository.save(new FilmActorEntity(fa));
    }
}
