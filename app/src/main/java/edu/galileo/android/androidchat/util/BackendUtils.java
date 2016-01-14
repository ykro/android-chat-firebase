package edu.galileo.android.androidchat.util;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.galileo.android.androidchat.entities.User;
import edu.galileo.android.androidchat.login.OnBackendTaskFinishedListener;

/**
 * Created by ykro.
 */
public class BackendUtils {
    private final static String FIREBASE_URL = "https://android-chat-example.firebaseio.com";
    private final static String FIREBASE_USERS_PATH = "users";
    private final static String FIREBASE_CHATS_PATH = "chats";
    private final static boolean OFFLINE = false;
    private final static boolean ONLINE = true;

    private Firebase dataReference;

    public BackendUtils(){
        dataReference = new Firebase(FIREBASE_URL);
    }

    public Firebase getDataReference() {
        return dataReference;
    }

    public String getAuthUserEmail() {
        AuthData authData = getAuthData();
        String email = null;
        if (authData != null) {
            Map<String, Object> providerData = authData.getProviderData();
            email = providerData.get("email").toString();
        }
        return email;
    }

    public void registerNewUser() {
        AuthData authData = getAuthData();
        if (authData != null) {
            Map<String,Object> providerData = authData.getProviderData();
            String email = providerData.get("email").toString();
            String newUserPath = email.replace(".","_");

            User currentUser = new User(email, true, null);
            getUsersReference(newUserPath).setValue(currentUser);
        }
    }

    public void changeUserConnectionStatus(final boolean online) {
        AuthData authData = getAuthData();
        if (authData != null) {

            Map<String, Object> providerData = authData.getProviderData();
            String email = providerData.get("email").toString();
            String userPath = email.replace(".", "_");
            final Firebase userReference = getUsersReference(userPath);

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    User currentUser = snapshot.getValue(User.class);
                    if (currentUser != null) {

                        currentUser.setOnline();
                        Map<String, Object> updates = new HashMap<String, Object>();
                        updates.put("online", online);
                        userReference.updateChildren(updates);
                    } else {
                        registerNewUser();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
    }

    public void signUp(final String email, final String password, final OnBackendTaskFinishedListener listener) {
        dataReference.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                listener.onSignUpSuccess();
                signIn(email, password, listener);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                listener.onSignUpError(firebaseError.getMessage());
            }
        });
    }

    public void signIn(String email, String password, final OnBackendTaskFinishedListener listener) {
        dataReference.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                changeUserConnectionStatus(ONLINE);
                listener.onSignInSuccess();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.onSignInError(firebaseError.getMessage());
            }
        });

    }

    public void signOff() {
        changeUserConnectionStatus(OFFLINE);
        dataReference.unauth();
        //callback();
    }

    private AuthData getAuthData() {
        return dataReference.getAuth();
    }

    private Firebase getUsersReference(String user){
        return dataReference.getRoot().child(FIREBASE_USERS_PATH).child(user);
    }

    public void checkAlreadyAuthenticated(OnBackendTaskFinishedListener listener) {
        if (getAuthData() != null) {
            listener.onSignInSuccess();
        }
    }
}
