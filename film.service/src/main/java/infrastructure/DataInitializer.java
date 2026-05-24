package infrastructure;

import domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RegizorRepository regizorRepository;
    private final ScenaristRepository scenaristRepository;
    private final ProducatorRepository producatorRepository;
    private final FilmRepository filmRepository;
    private final FilmImagineRepository filmImagineRepository;

    public DataInitializer(RegizorRepository regizorRepository,
                           ScenaristRepository scenaristRepository,
                           ProducatorRepository producatorRepository,
                           FilmRepository filmRepository,
                           FilmImagineRepository filmImagineRepository) {
        this.regizorRepository = regizorRepository;
        this.scenaristRepository = scenaristRepository;
        this.producatorRepository = producatorRepository;
        this.filmRepository = filmRepository;
        this.filmImagineRepository = filmImagineRepository;
    }

    @Override
    public void run(String... args) {
        if (filmRepository.count() > 0) return;

        RegizorEntity r1 = saveRegizor("Nolan", "Christopher", 1970, "Britanic",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/Christopher_Nolan_Cannes_2018.jpg/220px-Christopher_Nolan_Cannes_2018.jpg");
        RegizorEntity r2 = saveRegizor("Călinescu", "Andrei", 1975, "Român", null);

        ScenaristEntity s1 = saveScenarist("Sorkin", "Aaron", 1961, "American", null);
        ScenaristEntity s2 = saveScenarist("Munteanu", "Elena", 1985, "Română", null);

        ProducatorEntity p1 = saveProducator("Heyman", "David", 1961, "Britanic",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/David_Heyman_2013.jpg/220px-David_Heyman_2013.jpg",
                "Heyday Films");
        ProducatorEntity p2 = saveProducator("Georgescu", "Mihai", 1972, "Român", null, "MediaPro");

        saveFilm("Inception", 2010, TipFilm.ARTISTIC, CategorieFilm.SF, r1, s1, p1,
                "Un hoț care fură secrete corporative prin tehnologia de partajare a viselor.");
        saveFilm("Dacia Liberă", 2020, TipFilm.ARTISTIC, CategorieFilm.DRAMA, r2, s2, p2,
                "Drama istorică despre un sat românesc în perioada interbelică.");
        saveFilm("Detectivul de Noapte", 2023, TipFilm.SERIAL, CategorieFilm.ACTIUNE, r2, s2, p1,
                "Serial polițist cu episoade săptămânale.");

        filmRepository.findAll().forEach(f -> {
            FilmImagineEntity img1 = new FilmImagineEntity();
            img1.setIdFilm(f.getId());
            img1.setCaleImagine("https://picsum.photos/seed/film" + f.getId() + "a/400/250");
            filmImagineRepository.save(img1);
            FilmImagineEntity img2 = new FilmImagineEntity();
            img2.setIdFilm(f.getId());
            img2.setCaleImagine("https://picsum.photos/seed/film" + f.getId() + "b/400/250");
            filmImagineRepository.save(img2);
        });

        System.out.println("[FilmService] Date demo initializate.");
    }

    private RegizorEntity saveRegizor(String nume, String prenume, int an, String nat, String foto) {
        RegizorEntity e = new RegizorEntity();
        e.setNume(nume);
        e.setPrenume(prenume);
        e.setAnNastere(an);
        e.setNationalitate(nat);
        e.setFoto(foto);
        return regizorRepository.save(e);
    }

    private ScenaristEntity saveScenarist(String nume, String prenume, int an, String nat, String foto) {
        ScenaristEntity e = new ScenaristEntity();
        e.setNume(nume);
        e.setPrenume(prenume);
        e.setAnNastere(an);
        e.setNationalitate(nat);
        e.setFoto(foto);
        return scenaristRepository.save(e);
    }

    private ProducatorEntity saveProducator(String nume, String prenume, int an, String nat, String foto, String companie) {
        ProducatorEntity e = new ProducatorEntity();
        e.setNume(nume);
        e.setPrenume(prenume);
        e.setAnNastere(an);
        e.setNationalitate(nat);
        e.setFoto(foto);
        e.setCompanie(companie);
        return producatorRepository.save(e);
    }

    private void saveFilm(String titlu, int an, TipFilm tip, CategorieFilm cat,
                          RegizorEntity regizor, ScenaristEntity scenarist, ProducatorEntity producator,
                          String descriere) {
        FilmEntity f = new FilmEntity();
        f.setTitlu(titlu);
        f.setAnRealizare(an);
        f.setTipFilm(tip);
        f.setCategorieFilm(cat);
        f.setRegizorId(regizor.getId());
        f.setScenaristId(scenarist.getId());
        f.setProducatorId(producator.getId());
        f.setDescriere(descriere);
        filmRepository.save(f);
    }
}
