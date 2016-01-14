package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface OnBackendTaskFinishedListener {
    void onSignInSuccess();
    void onSignUpSuccess();
    void onSignInError(String error);
    void onSignUpError(String error);
}
