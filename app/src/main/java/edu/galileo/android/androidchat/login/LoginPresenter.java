package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface LoginPresenter {
    void registerNewUser(String email, String password);
    void validateLogin(String email, String password);
    void checkForAuthenticatedUser();
    void onDestroy();
}
