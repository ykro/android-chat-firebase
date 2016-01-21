package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */

public class LoginInteractorImpl implements LoginInteractor {
    private UserRepository util;

    public LoginInteractorImpl() {
        this.util = UserRepository.getInstance();
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
