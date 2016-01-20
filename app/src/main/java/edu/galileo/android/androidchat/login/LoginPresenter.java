package edu.galileo.android.androidchat.login;

import edu.galileo.android.androidchat.events.LoginEvent;

/**
 * Created by ykro.
 */
public interface LoginPresenter {
    void onCreate();
    void onDestroy();
    void onEvent(LoginEvent event);
    void checkForAuthenticatedUser();
    void validateLogin(String email, String password);
    void registerNewUser(String email, String password);
}
