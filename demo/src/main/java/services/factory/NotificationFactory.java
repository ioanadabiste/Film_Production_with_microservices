package services.factory;

import domain.Notification;
import domain.NotificationType;
import domain.User;

public class NotificationFactory {

    /**
     * Factory Method – creeaza notificarea potrivita in functie de tip.
     * In loc de switch-uri imprastiate prin cod, toata logica e centralizata aici.
     */
    public static Notification create(NotificationType type, User user, String message) {
        return switch (type) {
            case EMAIL    -> new Notification(type, user.getId().getUserId(), message);
            case SMS      -> new Notification(type, user.getId().getUserId(), message);
            case WHATSAPP -> new Notification(type, user.getId().getUserId(), message);
        };
    }
}