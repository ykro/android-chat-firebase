package edu.galileo.android.androidchat.login;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.galileo.android.androidchat.repositories.RepositoryHelper;
import edu.galileo.android.androidchat.contactlist.ContactListRepository;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class UserRepository {
    private User currentUser;
    private RepositoryHelper repositoryHelper;
    private Firebase dataReference;
    private final static String USERS_PATH = "users";

    private static class SingletonHolder {
        private static final UserRepository INSTANCE = new UserRepository();
    }
    public static UserRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public UserRepository(){
        repositoryHelper = RepositoryHelper.getInstance();
        dataReference = repositoryHelper.getDataReference();
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
                String email = repositoryHelper.getAuthUserEmail();
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
        String email = repositoryHelper.getAuthUserEmail();
        if (email != null) {
            this.currentUser = new User(email, true, null);
            getUserReference(email).setValue(currentUser);
        }
    }

    public void checkAlreadyAuthenticated() {
        if (dataReference.getAuth() != null) {
            String email = repositoryHelper.getAuthUserEmail();
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
        String email = repositoryHelper.getAuthUserEmail();
        if (email != null) {
            Firebase userReference = getUserReference(email);
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("online", online);
            userReference.updateChildren(updates);

            ContactListRepository contactListRepository = ContactListRepository.getInstance();
            contactListRepository.notifyContactsOfConnectionChange(online);

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
