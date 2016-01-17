package edu.galileo.android.androidchat.util;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.galileo.android.androidchat.entities.User;
import edu.galileo.android.androidchat.login.LoginTaskFinishedListener;

/**
 * Created by ykro.
 */
public class LoginUtil {
    private User currentUser;
    private Firebase dataReference;
    private BackendUtil backendUtil;

    private final static String USERS_PATH = "users";

    private static class SingletonHolder {
        private static final LoginUtil INSTANCE = new LoginUtil();
    }
    public static LoginUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public LoginUtil(){
        backendUtil = BackendUtil.getInstance();
        dataReference = backendUtil.getDataReference();
    }

    public void signUp(final String email, final String password, final LoginTaskFinishedListener listener) {
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

    public void signIn(String email, String password, final LoginTaskFinishedListener listener) {
        dataReference.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                String email = backendUtil.getAuthUserEmail();
                if (email != null) {
                    Firebase userReference = getUserReference(email);
                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            currentUser = snapshot.getValue(User.class);
                            if (currentUser == null) {
                                registerNewUser();
                            }
                            changeUserConnectionStatus(User.ONLINE);
                            listener.onSignInSuccess();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                listener.onSignInError(firebaseError.getMessage());
            }
        });

    }

    public void signOff() {
        changeUserConnectionStatus(User.OFFLINE);
        dataReference.unauth();
    }

    public void registerNewUser() {
        String email = backendUtil.getAuthUserEmail();
        if (email != null) {
            this.currentUser = new User(email, true, null);
            getUserReference(email).setValue(currentUser);
        }
    }

    public void checkAlreadyAuthenticated(final LoginTaskFinishedListener listener) {
        if (dataReference.getAuth() != null) {
            String email = backendUtil.getAuthUserEmail();
            if (email != null) {
                Firebase userReference = getUserReference(email);
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        currentUser = snapshot.getValue(User.class);
                        if (currentUser == null) {
                            registerNewUser();
                        }
                        changeUserConnectionStatus(User.ONLINE);
                        listener.onSignInSuccess();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        listener.onSignInError(firebaseError.getMessage());
                    }
                });
            }
        } else {
            listener.onFailedToRecoverSession();
        }
    }

    public void changeUserConnectionStatus(boolean online) {
        String email = currentUser.getEmail();
        Firebase userReference = getUserReference(email);
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("online", online);
        userReference.updateChildren(updates);

        ContactListUtil contactListUtil = ContactListUtil.getInstance();
        contactListUtil.notifyContactsOfConnectionChange(online);
    }

    public Firebase getUserReference(String email){
        String emailKey = email.replace(".","_");
        return dataReference.getRoot().child(USERS_PATH).child(emailKey);
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
