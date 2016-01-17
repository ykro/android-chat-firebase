package edu.galileo.android.androidchat.util;

import com.firebase.client.Firebase;

/**
 * Created by ykro.
 */
public class BackendUtil {
    private final static String FIREBASE_URL = "https://android-chat-example.firebaseio.com";
    private final static String CHATS_PATH = "chats";

    private Firebase dataReference;

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
}
