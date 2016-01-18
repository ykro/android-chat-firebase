package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface LoginPresenter {
    void onPause();
    void onResume();
    void onCreate();
    void onDestroy();
    void onEvent(LoginEvent event);
    void checkForAuthenticatedUser();
    void validateLogin(String email, String password);
    void registerNewUser(String email, String password);
}
