package controllers;

import domain.Producator;
import infrastructure.ProducatorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ProducatorService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/Producator")
public class ProducatorController {

    private final ProducatorService producatorService;

    public ProducatorController(ProducatorRepository repo) {
        this.producatorService = new ProducatorService(repo);
    }

    @GetMapping
    public ResponseEntity<List<Producator>> getAll() {
        return ResponseEntity.ok(producatorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producator> getById(@PathVariable int id) {
        Optional<Producator> p = producatorService.getById(id);
        return p.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Producator p) {
        return producatorService.insert(p)
                ? ResponseEntity.ok("Producator adaugat!")
                : ResponseEntity.badRequest().body("Eroare!");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Producator p) {
        return producatorService.update(p)
                ? ResponseEntity.ok("Producator actualizat!")
                : ResponseEntity.badRequest().body("Producatorul nu exista!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return producatorService.delete(id)
                ? ResponseEntity.ok("Producator sters!")
                : ResponseEntity.notFound().build();
    }
}
