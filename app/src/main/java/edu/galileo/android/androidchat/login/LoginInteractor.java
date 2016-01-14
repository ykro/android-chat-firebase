package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface LoginInteractor {
    void checkAlreadyAuthenticated();
    void doSignUp(String email, String password);
    void doSignIn(String email, String password);
}
