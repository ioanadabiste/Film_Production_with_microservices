package controllers;

import domain.FilmActor;
import infrastracture.FilmActorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.FilmActorService;

import java.util.List;

@RestController
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
@RequestMapping("/api/FilmActor")
public class FilmActorController {

    private final FilmActorService filmActorService;

    public FilmActorController(FilmActorRepository repo) {
        this.filmActorService = new FilmActorService(repo);
    }

    @GetMapping("/byFilm/{idFilm}")
    public ResponseEntity<List<FilmActor>> getByFilm(
            @PathVariable int idFilm) {
        return ResponseEntity.ok(filmActorService.getByFilmId(idFilm));
    }

    @GetMapping("/byActor/{idActor}")
    public ResponseEntity<List<FilmActor>> getByActor(
            @PathVariable int idActor) {
        return ResponseEntity.ok(filmActorService.getByActorId(idActor));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody FilmActor fa) {
        if (fa == null || fa.getIdFilm() <= 0 || fa.getIdActor() <= 0) {
            return ResponseEntity.badRequest().body("idFilm si idActor sunt obligatorii (> 0).");
        }
        return filmActorService.insert(fa)
                ? ResponseEntity.ok("Relatie adaugata!")
                : ResponseEntity.badRequest().body("Nu s-a putut salva legatura film-actor.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return filmActorService.delete(id)
                ? ResponseEntity.ok("Relație ștearsă!")
                : ResponseEntity.notFound().build();
    }
}