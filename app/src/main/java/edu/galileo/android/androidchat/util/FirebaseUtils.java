package edu.galileo.android.androidchat.util;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class FirebaseUtils {
    private final static String FIREBASE_URL = "https://android-chat-example.firebaseio.com";
    private final static String FIREBASE_USERS_PATH = "users";
    private final static String FIREBASE_CHATS_PATH = "chats";
    private Firebase dataReference;

    public FirebaseUtils(){
        dataReference = new Firebase(FIREBASE_URL);
    }

    public Firebase getDataReference() {
        return dataReference;
    }

    public static String getFirebaseURL() {
        return FIREBASE_URL;
    }

    public static String getFirebaseUsersPath() {
        return FIREBASE_USERS_PATH;
    }

    public static String getFirebaseChatsPath() {
        return FIREBASE_CHATS_PATH;
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
        Map<String,Object> providerData = authData.getProviderData();
        String email = providerData.get("email").toString();
        String newUserPath = email.replace(".","_");

        User currentUser = new User(email, true, null);
        getUsersReference(newUserPath).setValue(currentUser);
    }

    public void changeUserConnectionStatus(final boolean online) {
        AuthData authData = getAuthData();

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
                    updates.put("online", online);
                    userReference.updateChildren(updates);
                } else {
                    registerNewUser();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    private AuthData getAuthData() {
        return dataReference.getAuth();
    }

    private Firebase getUsersReference(String user){
        return dataReference.getRoot().child(FirebaseUtils.getFirebaseUsersPath()).child(user);
    }
}
