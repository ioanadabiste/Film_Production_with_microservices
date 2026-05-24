package controllers;

import domain.FilmStatistics;
import infrastracture.FilmStatisticRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.FilmStatisticService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Statistic")
@CrossOrigin(origins = "http://localhost:3000")
public class FilmStatisticController {

    private final FilmStatisticService service;

    public FilmStatisticController(FilmStatisticRepository repo) {
        this.service = new FilmStatisticService(repo);
    }

    @GetMapping
    public ResponseEntity<List<FilmStatistics>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmStatistics> getById(@PathVariable int id) {
        Optional<FilmStatistics> fs = service.getById(id);
        return fs.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byFilm/{idFilm}")
    public ResponseEntity<FilmStatistics> getByFilm(
            @PathVariable int idFilm) {
        FilmStatistics fs = service.getByFilmId(idFilm);
        return fs != null
                ? ResponseEntity.ok(fs)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/topRating")
    public ResponseEntity<List<FilmStatistics>> topRating() {
        return ResponseEntity.ok(service.getTopByRating());
    }

    @GetMapping("/topVizualizari")
    public ResponseEntity<List<FilmStatistics>> topVizualizari() {
        return ResponseEntity.ok(service.getTopByVizualizari());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody FilmStatistics fs) {
        return service.insert(fs)
                ? ResponseEntity.ok("Statistică adăugată!")
                : ResponseEntity.badRequest().body("Eroare!");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody FilmStatistics fs) {
        return service.update(fs)
                ? ResponseEntity.ok("Statistică actualizată!")
                : ResponseEntity.badRequest().body("Nu există!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return service.delete(id)
                ? ResponseEntity.ok("Statistică ștearsă!")
                : ResponseEntity.notFound().build();
    }
}