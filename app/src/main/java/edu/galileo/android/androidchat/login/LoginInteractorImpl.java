package edu.galileo.android.androidchat.login;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import edu.galileo.android.androidchat.util.FirebaseUtils;

/**
 * Created by ykro.
 */
public class LoginInteractorImpl implements LoginInteractor {
    private FirebaseUtils firebase;
    private Firebase dataReference;

    private final static boolean ONLINE = true;
    private final static boolean OFFLINE = true;

    public LoginInteractorImpl() {
        this.firebase = new FirebaseUtils();
        this.dataReference = firebase.getDataReference();
    }

    @Override
    public void doSignUp(final String email, final String password, final OnServerTaskFinishedListener listener) {
        dataReference.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                listener.onSignUpSuccess();
                doSignIn(email, password, listener);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                listener.onSignUpError(firebaseError.getMessage());
            }
        });
    }

    @Override
    public void doSignIn(String email, String password, final OnServerTaskFinishedListener listener) {
        dataReference.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                firebase.changeUserConnectionStatus(ONLINE);
                listener.onSignInSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.onSignInError(firebaseError.getMessage());
            }
        });
    }

    @Override
    public void checkAlreadyAuthenticated(OnServerTaskFinishedListener listener) {
        if (this.dataReference.getAuth() != null) {
            listener.onSignInSuccess();
        }
    }

    @Override
    public void doSignOff() {
        firebase.changeUserConnectionStatus(OFFLINE);
        dataReference.unauth();
    }
}
