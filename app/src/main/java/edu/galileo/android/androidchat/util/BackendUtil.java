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


    private Firebase dataReference;
    private ChildEventListener contactListEventListener;

    public BackendUtil(){
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
                changeUserConnectionStatus(User.ONLINE);
                listener.onSignInSuccess();
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
        //callback();
    }

    private AuthData getAuthData() {
        return dataReference.getAuth();
    }

    private Firebase getUsersReference(String user){
        return dataReference.getRoot().child(USERS_PATH).child(user);
    }

    private Firebase getContactsReference(String user){
        return dataReference.getRoot().child(USERS_PATH).child(user).child(CONTACTS_PATH);
    }

    public void checkAlreadyAuthenticated(LoginTaskFinishedListener listener) {
        if (getAuthData() != null) {
            listener.onSignInSuccess();
        }
    }

    public void subscribeForContactListUpdates(final ContactListTaskFinishedListener listener){
        if (contactListEventListener != null) {
            contactListEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                    Log.e("ASDF", dataSnapshot.getKey() + " " + dataSnapshot.getValue());
                    String key = dataSnapshot.getKey();
                    key = key.replace(".","_");
                    //getUserFromKey(key);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                    listener.onContactChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listener.onContactRemoved();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    listener.onSubscriptionError(firebaseError.getMessage());
                }
            };

            String email = getAuthUserEmail();
            email = email.replace(".","_");
            getContactsReference(email).addChildEventListener(contactListEventListener);
        }
    }

    private void getUserFromKey(String key, final ContactListTaskFinishedListener listener) {
        getUsersReference(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                listener.onContactAdded(user);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void unSubscribeForContactListUpdates(){
        if (contactListEventListener != null) {
            dataReference.removeEventListener(contactListEventListener);
            contactListEventListener = null;
        }
    }

    public static String getAvatarUrl(String username) {
        return GRAVATAR_URL + md5(username) + "?s=72";
    }

    public static final String md5(final String s) {
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
