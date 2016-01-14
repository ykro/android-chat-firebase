package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public interface LoginTaskFinishedListener {
    void onSignInSuccess();
    void onSignUpSuccess();
    void onSignInError(String error);
    void onSignUpError(String error);
}
