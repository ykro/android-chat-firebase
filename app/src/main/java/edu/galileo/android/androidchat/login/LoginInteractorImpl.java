package edu.galileo.android.androidchat.login;

import edu.galileo.android.androidchat.util.LoginUtil;

/**
 * Created by ykro.
 */

public class LoginInteractorImpl implements LoginInteractor {
    private LoginUtil util;
    private LoginTaskFinishedListener listener;

    public LoginInteractorImpl(LoginTaskFinishedListener listener) {
        this.util = LoginUtil.getInstance();
        this.listener = listener;
    }

    @Override
    public void doSignUp(final String email, final String password) {
        util.signUp(email, password, listener);
    }

    @Override
    public void doSignIn(String email, String password) {
        util.signIn(email, password, listener);
    }

    @Override
    public void checkAlreadyAuthenticated() {
        util.checkAlreadyAuthenticated(listener);
    }
}
