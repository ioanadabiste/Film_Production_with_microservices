package services.observer;

import domain.User;

public class WhatsAppNotificationObserver implements UserChangeObserver {

    @Override
    public void onUserChanged(User user, String changeDescription) {
        System.out.println("[WHATSAPP] Notificare trimisa la " + user.getPhone()
                + " : " + changeDescription);
    }
}