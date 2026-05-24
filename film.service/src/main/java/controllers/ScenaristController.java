package controllers;

import domain.Scenarist;
import infrastructure.ScenaristRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ScenaristService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/Scenarist")
public class ScenaristController {

    private final ScenaristService scenaristService;

    public ScenaristController(ScenaristRepository repo) {
        this.scenaristService = new ScenaristService(repo);
    }

    @GetMapping
    public ResponseEntity<List<Scenarist>> getAll() {
        return ResponseEntity.ok(scenaristService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scenarist> getById(@PathVariable int id) {
        Optional<Scenarist> s = scenaristService.getById(id);
        return s.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Scenarist s) {
        return scenaristService.insert(s)
                ? ResponseEntity.ok("Scenarist adăugat!")
                : ResponseEntity.badRequest().body("Eroare!");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Scenarist s) {
        return scenaristService.update(s)
                ? ResponseEntity.ok("Scenarist actualizat!")
                : ResponseEntity.badRequest().body("Scenaristul nu există!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return scenaristService.delete(id)
                ? ResponseEntity.ok("Scenarist șters!")
                : ResponseEntity.notFound().build();
    }
}