package edu.galileo.android.androidchat.login;

import edu.galileo.android.androidchat.util.BackendUtil;

/**
 * Created by ykro.
 */

public class LoginInteractorImpl implements LoginInteractor {
    private BackendUtil backendUtil;
    private LoginTaskFinishedListener listener;

    public LoginInteractorImpl(LoginTaskFinishedListener listener) {
        this.backendUtil = new BackendUtil();
        this.listener = listener;
    }

    @Override
    public void doSignUp(final String email, final String password) {
        backendUtil.signUp(email, password, listener);
    }

    @Override
    public void doSignIn(String email, String password) {
        backendUtil.signIn(email, password, listener);
    }

    @Override
    public void checkAlreadyAuthenticated() {
        backendUtil.checkAlreadyAuthenticated(listener);
    }
}
