package edu.galileo.android.androidchat.util;

import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import edu.galileo.android.androidchat.contactlist.ContactListTaskFinishedListener;
import edu.galileo.android.androidchat.entities.User;
import edu.galileo.android.androidchat.login.LoginTaskFinishedListener;

/**
 * Created by ykro.
 */
public class BackendUtil {
    private final static String GRAVATAR_URL = "http://www.gravatar.com/avatar/";
    private final static String FIREBASE_URL = "https://android-chat-example.firebaseio.com";
    private final static String CONTACTS_PATH = "contacts";
    private final static String USERS_PATH = "users";
    private final static String CHATS_PATH = "chats";

    private User currentUser;
    private Firebase dataReference;
    private ChildEventListener contactListEventListener;

    private static class SingletonHolder {
        private static final BackendUtil INSTANCE = new BackendUtil();
    }

    public static BackendUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public BackendUtil(){
        dataReference = new Firebase(FIREBASE_URL);
    }

    public Firebase getDataReference() {
        return dataReference;
    }

    public String getAuthUserEmail() {
        AuthData authData = dataReference.getAuth();
        String email = null;
        if (authData != null) {
            Map<String, Object> providerData = authData.getProviderData();
            email = providerData.get("email").toString();
        }
        return email;
    }

    public void registerNewUser() {
        String email = getAuthUserEmail();
        if (email != null) {
            this.currentUser = new User(email, true, null);
            getUsersReference(email).setValue(currentUser);
        }
    }

    public void changeUserConnectionStatus(boolean online) {
        String email = currentUser.getEmail();
        Firebase userReference = getUsersReference(email);
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("online", online);
        userReference.updateChildren(updates);
        notifyContactsOfConnectionChange(online);
    }

    private void notifyContactsOfConnectionChange(boolean online) {
        Firebase reference;
        Map<String, Boolean> contacts = currentUser.getContacts();

        if (contacts != null) {
            for(Map.Entry<String, Boolean> entry : contacts.entrySet()) {
                String email = entry.getKey();
                reference = getOneContactReference(email, currentUser.getEmail());
                Map<String, Object> updates = new HashMap<String, Object>();
                updates.put("online", online);
                reference.updateChildren(updates);
            }
        }
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
                String email = getAuthUserEmail();
                if (email != null) {
                    Firebase userReference = getUsersReference(email);
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

    private Firebase getUsersReference(String email){
        String key = email.replace(".","_");
        return dataReference.getRoot().child(USERS_PATH).child(key);
    }

    private Firebase getContactsReference(String email){
        String key = email.replace(".","_");
        return dataReference.getRoot().child(USERS_PATH).child(key).child(CONTACTS_PATH);
    }

    private Firebase getOneContactReference(String mainEmail, String childEmail){
        String mainKey = mainEmail.replace(".","_");
        String childKey = childEmail.replace(".","_");
        return dataReference.getRoot().child(USERS_PATH).child(mainKey).child(CONTACTS_PATH).child(childKey);
    }

    public void checkAlreadyAuthenticated(final LoginTaskFinishedListener listener) {
        if (dataReference.getAuth() != null) {
            String email = getAuthUserEmail();
            if (email != null) {
                Firebase userReference = getUsersReference(email);
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
    }

    public void subscribeForContactListUpdates(final ContactListTaskFinishedListener listener){
        if (contactListEventListener == null) {
            contactListEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                    String email = dataSnapshot.getKey();
                    boolean online = ((Boolean)dataSnapshot.getValue()).booleanValue();
                    User user = new User(email, online, null);
                    listener.onContactAdded(user);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                    String email = dataSnapshot.getKey();
                    boolean online = ((Boolean)dataSnapshot.getValue()).booleanValue();
                    User user = new User(email, online, null);
                    listener.onContactChanged(user);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String email = dataSnapshot.getKey();
                    boolean online = ((Boolean)dataSnapshot.getValue()).booleanValue();
                    User user = new User(email, online, null);
                    listener.onContactRemoved(user);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            };
            String email = currentUser.getEmail();
            getContactsReference(email).addChildEventListener(contactListEventListener);
        }
    }

    public void unSubscribeForContactListUpdates(){
        if (contactListEventListener != null) {
            dataReference.removeEventListener(contactListEventListener);
        }
    }

    public static String getAvatarUrl(String username) {
        return GRAVATAR_URL + md5(username) + "?s=72";
    }

    private static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
