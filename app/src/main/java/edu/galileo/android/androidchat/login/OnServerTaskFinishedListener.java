package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface OnServerTaskFinishedListener {
    void onSignInSuccess();
    void onSignUpSuccess();
    void onSignInError(String error);
    void onSignUpError(String error);
    void onSignOffSuccess();
}
