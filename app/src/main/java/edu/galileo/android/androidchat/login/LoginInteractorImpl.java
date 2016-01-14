package edu.galileo.android.androidchat.login;

import edu.galileo.android.androidchat.util.BackendUtils;

/**
 * Created by ykro.
 */

public class LoginInteractorImpl implements LoginInteractor {
    private BackendUtils firebase;
    private OnBackendTaskFinishedListener listener;

    public LoginInteractorImpl(OnBackendTaskFinishedListener listener) {
        this.firebase = new BackendUtils();
        this.listener = listener;
    }

    @Override
    public void doSignUp(final String email, final String password) {
        firebase.signUp(email, password, listener);
    }

    @Override
    public void doSignIn(String email, String password) {
        firebase.signIn(email, password, listener);
    }

    @Override
    public void checkAlreadyAuthenticated() {
        firebase.checkAlreadyAuthenticated(listener);
    }
}
