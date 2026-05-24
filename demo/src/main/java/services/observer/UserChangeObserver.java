package services.observer;

import domain.User;

public interface UserChangeObserver {
    void onUserChanged(User user, String changeDescription);
}