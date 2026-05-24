package controllers;

import domain.*;
import domain.DAOContracts.IUserDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.UsersService;
import services.factory.NotificationFactory;

import java.util.List;

@RestController
@RequestMapping("/api/User")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class UserController {

    private final UsersService usersService;

    public UserController(IUserDAO userDAO) {
        this.usersService = new UsersService(userDAO);
    }

    @GetMapping
    public ResponseEntity<List<User>> Users() {
        return ResponseEntity.ok(usersService.Users());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> UserById(@PathVariable int id) {
        User user = usersService.UserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/byType/{userType}")
    public ResponseEntity<List<User>> UsersByType(@PathVariable int userType) {
        return ResponseEntity.ok(usersService.UsersByType(userType));
    }

    @GetMapping("/export")
    public ResponseEntity<String> ExportToCsv() {
        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=utilizatori.csv")
                .body(usersService.ExportToCsv());
    }

    @PostMapping
    public ResponseEntity<String> Create(@RequestBody User user) {
        boolean ok = usersService.Insert(user);
        return ok ? ResponseEntity.ok("Utilizator adaugat!") : ResponseEntity.badRequest().body("Eroare!");
    }

    @PostMapping("/notify")
    public ResponseEntity<String> Notify(@RequestBody Notification notification) {
        // ── Factory Pattern: creeaza notificarea corecta in functie de tip ──
        User user = usersService.UserById(notification.getUserId());
        if (user == null) return ResponseEntity.badRequest().body("Utilizatorul nu exista!");

        Notification n = NotificationFactory.create(notification.getType(), user, notification.getMessage());
        boolean ok = usersService.NotifyUser(n);
        return ok ? ResponseEntity.ok("Notificare trimisa!") : ResponseEntity.badRequest().body("Eroare la trimitere!");
    }

    @PutMapping
    public ResponseEntity<String> Update(@RequestBody User user) {
        boolean ok = usersService.Update(user);
        return ok
                ? ResponseEntity.ok("Utilizator actualizat! Notificari trimise.")
                : ResponseEntity.badRequest().body("Utilizatorul nu exista!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> Delete(@PathVariable int id) {
        boolean ok = usersService.Delete(id);
        return ok ? ResponseEntity.ok("Utilizator sters!") : ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<User> Login(@RequestBody LoginRequest req) {
        User user = usersService.Login(req.getEmail(), req.getPassword());
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(401).build();
    }
}