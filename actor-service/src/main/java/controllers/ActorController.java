package controllers;

import domain.Actor;
import domain.DAOContracts.IActorDAO;
import infrastracture.ActorDAO;
import infrastracture.ActorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ActorService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/Actor")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorRepository repo) {
        IActorDAO actorDAO = new ActorDAO(repo);
        this.actorService = new ActorService(actorDAO);
    }

    @GetMapping
    public ResponseEntity<List<Actor>> getAll() {
        return ResponseEntity.ok(actorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> getById(@PathVariable int id) {
        Optional<Actor> a = actorService.getById(id);
        return a.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{nume}")
    public ResponseEntity<List<Actor>> search(@PathVariable String nume) {
        return ResponseEntity.ok(actorService.searchByName(nume));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Actor a) {
        return actorService.insert(a)
                ? ResponseEntity.ok("Actor adăugat!")
                : ResponseEntity.badRequest().body("Eroare!");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Actor a) {
        return actorService.update(a)
                ? ResponseEntity.ok("Actor actualizat!")
                : ResponseEntity.badRequest().body("Actorul nu există!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return actorService.delete(id)
                ? ResponseEntity.ok("Actor șters!")
                : ResponseEntity.notFound().build();
    }
}
