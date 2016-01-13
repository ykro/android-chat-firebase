package edu.galileo.android.androidchat.util;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.Map;

/**
 * Created by ykro.
 */
public class FirebaseUtils {
    private final static String FIREBASE_URL = "https://android-chat-example.firebaseio.com";
    private final static String FIREBASE_USERS_PATH = "users";
    private final static String FIREBASE_CHATS_PATH = "chats";

    public static String getFirebaseURL() {
        return FIREBASE_URL;
    }

    public static String getFirebaseUsersPath() {
        return FIREBASE_USERS_PATH;
    }

    public static String getFirebaseChatsPath() {
        return FIREBASE_CHATS_PATH;
    }

    public static String getAuthUserEmail() {
        AuthData authData = new Firebase(FIREBASE_URL).getAuth();
        String email = null;
        if (authData != null) {
            Map<String, Object> providerData = authData.getProviderData();
            email = providerData.get("email").toString();
        }
        return email;
    }
}
