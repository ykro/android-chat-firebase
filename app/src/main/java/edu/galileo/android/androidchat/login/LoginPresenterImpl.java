package edu.galileo.android.androidchat.login;

/**
 * Created by ykro.
 */
public class LoginPresenterImpl implements LoginPresenter,
                                           OnServerTaskFinishedListener {
    LoginView loginView;
    LoginInteractor loginInteractor;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void validateLogin(String email, String password) {
        if (loginView != null) {
            loginView.disableInputs();
            loginView.showProgress();
        }
        loginInteractor.doSignIn(email, password, this);
    }

    @Override
    public void registerNewUser(String email, String password) {
        if (loginView != null) {
            loginView.disableInputs();
            loginView.showProgress();
        }
        loginInteractor.doSignUp(email, password, this);
    }

    @Override
    public void checkForAuthenticatedUser() {
        loginInteractor.checkAlreadyAuthenticated(this);
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

    @Override
    public void onSignInSuccess() {
        if (loginView != null) {
            loginView.navigateToMainScreen();
        }
    }

    @Override
    public void onSignUpSuccess() {
        if (loginView != null) {
            loginView.newUserSuccess();
        }
    }

    @Override
    public void onSignInError(String error) {
        if (loginView != null) {
            loginView.loginError(error);
            loginView.hideProgress();
            loginView.enableInputs();
        }
    }

    @Override
    public void onSignUpError(String error) {
        if (loginView != null) {
            loginView.newUserError(error);
            loginView.hideProgress();
            loginView.enableInputs();
        }
    }

    @Override
    public void onSignOffSuccess() {

    }
}
