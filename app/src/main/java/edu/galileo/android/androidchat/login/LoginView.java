package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface LoginView {
    void showInputs();
    void hideInputs();
    void showProgress();
    void hideProgress();

    void handleSignUp();
    void handleSignIn();

    void navigateToMainScreen();
    void loginError(String error);

    void newUserSuccess();
    void newUserError(String error);
}
