package controllers;

import domain.Film;
import domain.DAOContracts.IFilmDAO;
import infrastructure.FilmDAO;
import infrastructure.FilmRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.LoggingFilmService;
import services.FilmService;
import services.export.CsvExportStrategy;
import services.export.JsonExportStrategy;
import services.export.XmlExportStrategy;
import services.export.DocExportStrategy;
import services.export.FilmExportStrategy;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/Film")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmRepository repo) {
        IFilmDAO filmDAO = new FilmDAO(repo);
        // Decorator Pattern (GoF): LoggingFilmService adauga logging la CRUD
        this.filmService = new LoggingFilmService(filmDAO);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        return ResponseEntity.ok(filmService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getById(@PathVariable int id) {
        Optional<Film> f = filmService.getById(id);
        return f.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{titlu}")
    public ResponseEntity<List<Film>> searchByTitle(@PathVariable String titlu) {
        return ResponseEntity.ok(filmService.searchByTitle(titlu));
    }

    @GetMapping("/filterByTip/{tip}")
    public ResponseEntity<List<Film>> filterByTip(@PathVariable String tip) {
        return ResponseEntity.ok(filmService.filterByTip(tip));
    }

    @GetMapping("/filterByCategorie/{categorie}")
    public ResponseEntity<List<Film>> filterByCategorie(@PathVariable String categorie) {
        return ResponseEntity.ok(filmService.filterByCategorie(categorie));
    }

    @GetMapping("/filterByAn/{an}")
    public ResponseEntity<List<Film>> filterByAn(@PathVariable int an) {
        return ResponseEntity.ok(filmService.filterByAn(an));
    }

    // ── EXPORT cu Strategy Pattern ─────────────────────────────────────────
    private ResponseEntity<String> exportWith(FilmExportStrategy strategy) {
        String content = strategy.export(filmService.getAll());
        return ResponseEntity.ok()
                .header("Content-Type", strategy.getContentType())
                .header("Content-Disposition", "attachment; filename=filme." + strategy.getFileExtension())
                .body(content);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<String> exportCsv() {
        return exportWith(new CsvExportStrategy());
    }

    @GetMapping("/export/json")
    public ResponseEntity<String> exportJson() {
        return exportWith(new JsonExportStrategy());
    }

    @GetMapping("/export/xml")
    public ResponseEntity<String> exportXml() {
        return exportWith(new XmlExportStrategy());
    }

    @GetMapping("/export/doc")
    public ResponseEntity<String> exportDoc() {
        return exportWith(new DocExportStrategy());
    }
    // ──────────────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Film f) {
        return filmService.insertAndReturn(f)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("Eroare!"));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody Film f) {
        return filmService.update(f)
                ? ResponseEntity.ok("Film actualizat!")
                : ResponseEntity.badRequest().body("Filmul nu există!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        return filmService.delete(id)
                ? ResponseEntity.ok("Film șters!")
                : ResponseEntity.notFound().build();
    }
}