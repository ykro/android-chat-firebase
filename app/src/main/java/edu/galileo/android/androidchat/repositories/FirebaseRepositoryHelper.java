package edu.galileo.android.androidchat.repositories;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class FirebaseRepositoryHelper {
    private User currentUser;
    private Firebase dataReference;
    private final static String SEPARATOR = "___";
    private final static String CHATS_PATH = "chats";
    private final static String USERS_PATH = "users";
    public final static String CONTACTS_PATH = "contacts";
    private final static String FIREBASE_URL = "https://android-chat-example.firebaseio.com";

    private static class SingletonHolder {
        private static final FirebaseRepositoryHelper INSTANCE = new FirebaseRepositoryHelper();
    }

    public static FirebaseRepositoryHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public FirebaseRepositoryHelper(){
        dataReference = new Firebase(FIREBASE_URL);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
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

    //revisar donde se usa
    public Firebase getUserReference(String email){
        Firebase userReference = null;
        if (email != null) {
            String emailKey = email.replace(".", "_");
            userReference = dataReference.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }

    public Firebase getMyUserReference() {
        return getUserReference(getAuthUserEmail());
    }

    public Firebase getContactsReference(String email){
        return getUserReference(email).child(CONTACTS_PATH);
    }

    public Firebase getMyContactsReference(){
        return getUserReference(getAuthUserEmail()).child(CONTACTS_PATH);
    }

    public Firebase getOneContactReference(String mainEmail, String childEmail){
        String childKey = childEmail.replace(".","_");
        return getUserReference(mainEmail).child(CONTACTS_PATH).child(childKey);
    }

    public Firebase getChatsReference(String receiver){
        String keySender = getAuthUserEmail().replace(".","_");
        String keyReceiver = receiver.replace(".","_");

        String keyChat = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyReceiver) > 0) {
            keyChat = keyReceiver + SEPARATOR + keySender;
        }
        return dataReference.getRoot().child(CHATS_PATH).child(keyChat);
    }

    public void changeUserConnectionStatus(boolean online) {
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("online", online);
        getMyUserReference().updateChildren(updates);

        notifyContactsOfConnectionChange(online);
    }

    public void notifyContactsOfConnectionChange(final boolean online) {
        Map<String, Boolean> contacts = currentUser.getContacts();

        if (contacts != null) {
            for(Map.Entry<String, Boolean> entry : contacts.entrySet()) {
                String email = entry.getKey();
                Firebase reference = getOneContactReference(email, currentUser.getEmail());
                reference.setValue(online);
            }
        }

        /*
        getMyContactsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot contact : snapshot.getChildren()) {
                    String email = contact.getKey();
                    Firebase reference = getOneContactReference(email, getAuthUserEmail());
                    reference.setValue(online);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
        */
    }
}
