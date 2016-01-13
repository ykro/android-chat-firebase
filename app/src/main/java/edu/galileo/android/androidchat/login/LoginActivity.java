package edu.galileo.android.androidchat.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.galileo.android.androidchat.R;
import edu.galileo.android.androidchat.contactlist.ContactListActivity;

public class LoginActivity extends AppCompatActivity
                           implements LoginView {
    @Bind(R.id.btnSignin) Button btnSignIn;
    @Bind(R.id.btnSignup) Button btnSignUp;

    @Bind(R.id.editTxtEmail) EditText inputEmail;
    @Bind(R.id.editTxtPassword) EditText inputPassword;

    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.layoutMainContainer) RelativeLayout container;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginPresenter = new LoginPresenterImpl(this);
        loginPresenter.checkForAuthenticatedUser();

    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    @OnClick(R.id.btnSignup)
    public void handleSignUp() {
        loginPresenter.registerNewUser(inputEmail.getText().toString(),
                inputPassword.getText().toString());
    }

    @Override
    @OnClick(R.id.btnSignin)
    public void handleSignIn() {
        loginPresenter.validateLogin(inputEmail.getText().toString(),
                inputPassword.getText().toString());
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void disableInputs() {
        enableInputs(false);
    }

    @Override
    public void enableInputs() {
        enableInputs(true);
    }

    @Override
    public void loginError(String error) {
        inputPassword.setText("");
        String msgError = String.format(getString(R.string.msg_error_signin), error);
        inputPassword.setError(msgError);
    }

    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, ContactListActivity.class));
    }

    @Override
    public void newUserError(String error) {
        inputPassword.setText("");
        String msgError = String.format(getString(R.string.msg_error_signup), error);
        inputPassword.setError(msgError);
    }

    @Override
    public void newUserSuccess() {
        Snackbar.make(container, R.string.msg_new_user, Snackbar.LENGTH_SHORT).show();
    }

    private void enableInputs(boolean status) {
        btnSignIn.setEnabled(status);
        btnSignUp.setEnabled(status);
        inputEmail.setEnabled(status);
        inputPassword.setEnabled(status);
    }

}
