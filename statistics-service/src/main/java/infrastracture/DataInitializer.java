package infrastracture;

import domain.FilmStatistics;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FilmStatisticRepository repository;

    public DataInitializer(FilmStatisticRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        save(1, "Inception", 1500000, 8.8);
        save(2, "Dacia Liberă", 320000, 7.5);
        save(3, "Detectivul de Noapte", 890000, 7.9);

        System.out.println("[StatisticsService] Statistici demo initializate.");
    }

    private void save(int idFilm, String titlu, int views, double rating) {
        FilmStatistics fs = new FilmStatistics();
        fs.setIdFilm(idFilm);
        fs.setTitluFilm(titlu);
        fs.setNrVizualizari(views);
        fs.setRating(rating);
        repository.save(new FilmStatisticEntity(fs));
    }
}
