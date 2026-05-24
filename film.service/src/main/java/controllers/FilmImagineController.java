package controllers;

import domain.FilmImagine;
import infrastructure.FilmImagineRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/FilmImagine")
public class FilmImagineController {

    private final service.FilmImagineService filmImagineService;

    public FilmImagineController(FilmImagineRepository repo) {
        this.filmImagineService = new service.FilmImagineService(repo);
    }

    @GetMapping("/byFilm/{idFilm}")
    public ResponseEntity<List<FilmImagine>> getByFilmId(
            @PathVariable int idFilm) {
        return ResponseEntity.ok(
                filmImagineService.getByFilmId(idFilm));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody FilmImagine fi) {
        return filmImagineService.insert(fi)
                ? ResponseEntity.ok("Imagine adăugată!")
                : ResponseEntity.badRequest().body("Eroare!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return filmImagineService.delete(id)
                ? ResponseEntity.ok("Imagine ștearsă!")
                : ResponseEntity.notFound().build();
    }
}