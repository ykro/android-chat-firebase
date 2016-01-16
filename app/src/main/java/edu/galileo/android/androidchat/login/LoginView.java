package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface LoginView {
    void showProgress();
    void hideProgress();
    void enableInputs();
    void disableInputs();

    void handleSignUp();
    void handleSignIn();

    void loginError(String error);
    void navigateToMainScreen();

    void newUserError(String error);
    void newUserSuccess();
}
