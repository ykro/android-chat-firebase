package edu.galileo.android.androidchat.login;

import edu.galileo.android.androidchat.api.UserAPI;

/**
 * Created by ykro.
 */

public class LoginInteractorImpl implements LoginInteractor {
    private UserAPI util;

    public LoginInteractorImpl() {
        this.util = UserAPI.getInstance();
    }

    @Override
    public void doSignUp(final String email, final String password) {
        util.signUp(email, password);
    }

    @Override
    public void doSignIn(String email, String password) {
        util.signIn(email, password);
    }

    @Override
    public void checkAlreadyAuthenticated() {
        util.checkAlreadyAuthenticated();
    }
}
