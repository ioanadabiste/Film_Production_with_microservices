package services.observer;

import domain.User;

public class SmsNotificationObserver implements UserChangeObserver {

    @Override
    public void onUserChanged(User user, String changeDescription) {
        System.out.println("[SMS] Notificare trimisa la " + user.getPhone()
                + " : " + changeDescription);
    }
}