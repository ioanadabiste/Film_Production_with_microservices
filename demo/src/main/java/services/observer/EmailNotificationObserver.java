package services.observer;

import domain.User;

public class EmailNotificationObserver implements UserChangeObserver {

    @Override
    public void onUserChanged(User user, String changeDescription) {
        // Aici poti integra un client SMTP real (ex: JavaMailSender)
        System.out.println("[EMAIL] Notificare trimisa la " + user.getEmail()
                + " : " + changeDescription);
    }
}