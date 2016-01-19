package edu.galileo.android.androidchat.api;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.login.LoginEvent;
import edu.galileo.android.androidchat.model.User;

/**
 * Created by ykro.
 */
public class UserAPI {
    private User currentUser;
    private APIHelper apiHelper;
    private Firebase dataReference;
    private final static String USERS_PATH = "users";

    private static class SingletonHolder {
        private static final UserAPI INSTANCE = new UserAPI();
    }
    public static UserAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public UserAPI(){
        apiHelper = APIHelper.getInstance();
        dataReference = apiHelper.getDataReference();
    }

    public void signUp(final String email, final String password) {
        dataReference.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                postEvent(LoginEvent.onSignUpSuccess);
                signIn(email, password);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                postEvent(LoginEvent.onSignUpError, firebaseError.getMessage());
            }
        });
    }

    public void signIn(String email, String password) {
        dataReference.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                String email = apiHelper.getAuthUserEmail();
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
                            postEvent(LoginEvent.onSignInSuccess);
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                postEvent(LoginEvent.onSignInError, firebaseError.getMessage());
            }
        });
    }

    public void signOff() {
        changeUserConnectionStatus(User.OFFLINE);
        dataReference.unauth();
    }

    public void registerNewUser() {
        String email = apiHelper.getAuthUserEmail();
        if (email != null) {
            this.currentUser = new User(email, true, null);
            getUserReference(email).setValue(currentUser);
        }
    }

    public void checkAlreadyAuthenticated() {
        if (dataReference.getAuth() != null) {
            String email = apiHelper.getAuthUserEmail();
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
                        postEvent(LoginEvent.onSignInSuccess);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        postEvent(LoginEvent.onSignInError, firebaseError.getMessage());
                    }
                });
            }
        } else {
            postEvent(LoginEvent.onFailedToRecoverSession);
        }
    }

    public void changeUserConnectionStatus(boolean online) {
        String email = apiHelper.getAuthUserEmail();
        if (email != null) {
            Firebase userReference = getUserReference(email);
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("online", online);
            userReference.updateChildren(updates);

            ContactListAPI contactListAPI = ContactListAPI.getInstance();
            contactListAPI.notifyContactsOfConnectionChange(online);

        }
    }

    public Firebase getUserReference(String email){
        String emailKey = email.replace(".","_");
        return dataReference.getRoot().child(USERS_PATH).child(emailKey);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private void postEvent(int type) {
        postEvent(type, null);
    }

    private void postEvent(int type, String errorMessage) {
        LoginEvent loginEvent = new LoginEvent();
        loginEvent.setEventType(type);
        if (errorMessage != null) {
            loginEvent.setErrorMesage(errorMessage);
        }

        EventBus eventBus = EventBus.getInstance();
        eventBus.post(loginEvent);
    }
}
