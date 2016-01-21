package edu.galileo.android.androidchat.repositories;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.Map;

/**
 * Created by ykro.
 */
public class RepositoryHelper {
    private final static String FIREBASE_URL = "https://android-chat-example.firebaseio.com";
    private Firebase dataReference;

    private static class SingletonHolder {
        private static final RepositoryHelper INSTANCE = new RepositoryHelper();
    }

    public static RepositoryHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public RepositoryHelper(){
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
}
