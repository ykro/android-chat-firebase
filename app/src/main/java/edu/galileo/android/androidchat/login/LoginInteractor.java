package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface LoginInteractor {
    void checkAlreadyAuthenticated(final OnServerTaskFinishedListener listener);
    void doSignUp(String email, String password, final OnServerTaskFinishedListener listener);
    void doSignIn(String email, String password, final OnServerTaskFinishedListener listener);
    void doSignOff();
}
