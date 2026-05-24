package services;

import domain.DAOContracts.IUserDAO;
import domain.Notification;
import domain.User;
import services.factory.NotificationFactory;
import services.observer.EmailNotificationObserver;
import services.observer.SmsNotificationObserver;
import services.observer.UserChangeObserver;
import services.observer.WhatsAppNotificationObserver;

import java.util.ArrayList;
import java.util.List;

public class UsersService {

    private final IUserDAO userDAO;

    // ── Observer Pattern: lista de observeri ───────────────────────────────
    private final List<UserChangeObserver> observers = new ArrayList<>(List.of(
            new EmailNotificationObserver(),
            new SmsNotificationObserver(),
            new WhatsAppNotificationObserver()
    ));

    public void addObserver(UserChangeObserver o)    { observers.add(o); }
    public void removeObserver(UserChangeObserver o) { observers.remove(o); }

    private void notifyObservers(User user, String msg) {
        observers.forEach(o -> o.onUserChanged(user, msg));
    }
    // ──────────────────────────────────────────────────────────────────────

    public UsersService(IUserDAO userDAO) { this.userDAO = userDAO; }

    public List<User> Users()                  { return userDAO.Users(); }
    public User UserById(int id)               { return userDAO.UserById(id); }
    public List<User> UsersByType(int userType){ return userDAO.UsersByType(userType); }
    public boolean Insert(User user)           { return userDAO.Insert(user); }

    public boolean Update(User user) {
        boolean result = userDAO.Update(user);
        if (result) {
            // Observer Pattern: notifica toti observerii inregistrati
            notifyObservers(user,
                    "Informatiile tale de autentificare au fost modificate. " +
                            "Daca nu ai initiat aceasta schimbare, contacteaza administratorul.");
        }
        return result;
    }

    public boolean Delete(int id) { return userDAO.Delete(id); }

    public String ExportToCsv() {
        List<User> users = userDAO.Users();
        StringBuilder sb = new StringBuilder("id,nume,prenume,email,tip\n");
        for (User u : users) {
            String tip = switch (u.getUserType()) {
                case 0 -> "Angajat";
                case 1 -> "Manager";
                case 2 -> "Administrator";
                default -> String.valueOf(u.getUserType());
            };
            sb.append(u.getId().getUserId()).append(",")
                    .append(u.getName()).append(",")
                    .append(u.getSurname()).append(",")
                    .append(u.getEmail()).append(",")
                    .append(tip).append("\n");
        }
        return sb.toString();
    }

    public boolean NotifyUser(Notification notification) {
        User user = userDAO.UserById(notification.getUserId());
        if (user == null) return false;
        Notification n = NotificationFactory.create(notification.getType(), user, notification.getMessage());
        UserChangeObserver target = switch (n.getType()) {
            case EMAIL -> new EmailNotificationObserver();
            case SMS -> new SmsNotificationObserver();
            case WHATSAPP -> new WhatsAppNotificationObserver();
        };
        target.onUserChanged(user, n.getMessage());
        return true;
    }

    public User Login(String email, String password) {
        return userDAO.Login(email, password);
    }
}