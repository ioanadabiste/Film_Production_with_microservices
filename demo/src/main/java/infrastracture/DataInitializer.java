package infrastracture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository repository;

    public DataInitializer(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        save("Ion", "Popescu", "angajat@firma.ro", "angajat123", "0711111111", 0);
        save("Maria", "Ionescu", "manager@firma.ro", "manager123", "0722222222", 1);
        save("Admin", "Sistem", "admin@firma.ro", "admin123", "0733333333", 2);
        System.out.println("[UserService] Utilizatori demo initializati.");
    }

    private void save(String name, String surname, String email, String password, String phone, int userType) {
        UserEntity u = new UserEntity();
        u.setName(name);
        u.setSurname(surname);
        u.setEmail(email);
        u.setPassword(password);
        u.setPhone(phone);
        u.setUserType(userType);
        repository.save(u);
    }
}
