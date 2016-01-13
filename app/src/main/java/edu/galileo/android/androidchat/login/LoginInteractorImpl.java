package edu.galileo.android.androidchat.login;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.galileo.android.androidchat.entities.User;
import edu.galileo.android.androidchat.util.FirebaseUtils;

/**
 * Created by ykro.
 */
public class LoginInteractorImpl implements LoginInteractor {
    Firebase dataReference;

    public LoginInteractorImpl() {
        this.dataReference = new Firebase(FirebaseUtils.getFirebaseURL());
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
                changeOnlineStatus();
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
        dataReference.unauth();
    }

    private void changeOnlineStatus() {
        AuthData authData = dataReference.getAuth();

        Map<String,Object> providerData = authData.getProviderData();
        String email = providerData.get("email").toString();
        String userPath = email.replace(".","_");
        final Firebase userReference = getUsersReference(userPath);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User currentUser = snapshot.getValue(User.class);
                if (currentUser != null){

                    currentUser.setOnline();
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("online", true);
                    userReference.updateChildren(updates);
                } else {
                    registerNewUser();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    private void registerNewUser() {
        AuthData authData = dataReference.getAuth();
        Map<String,Object> providerData = authData.getProviderData();
        String email = providerData.get("email").toString();
        String newUserPath = email.replace(".","_");

        User currentUser = new User(email, true, null);
        getUsersReference(newUserPath).setValue(currentUser);
    }

    private Firebase getUsersReference(String user){
        return dataReference.getRoot().child(FirebaseUtils.getFirebaseUsersPath()).child(user);
    }
}
