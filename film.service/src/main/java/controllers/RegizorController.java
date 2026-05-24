package controllers;
import domain.Regizor;
import infrastructure.RegizorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.RegizorService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/Regizor")
public class RegizorController {

    private final RegizorService regizorService;

    public RegizorController(RegizorRepository repo) {
        this.regizorService = new RegizorService(repo);
    }

    @GetMapping
    public ResponseEntity<List<Regizor>> getAll() {
        return ResponseEntity.ok(regizorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Regizor> getById(@PathVariable int id) {
        Optional<Regizor> r = regizorService.getById(id);
        return r.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Regizor r) {
        return regizorService.insert(r)
                ? ResponseEntity.ok("Regizor adăugat!")
                : ResponseEntity.badRequest().body("Eroare!");
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Regizor r) {
        return regizorService.update(r)
                ? ResponseEntity.ok("Regizor actualizat!")
                : ResponseEntity.badRequest().body("Regizorul nu există!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return regizorService.delete(id)
                ? ResponseEntity.ok("Regizor șters!")
                : ResponseEntity.notFound().build();
    }
}