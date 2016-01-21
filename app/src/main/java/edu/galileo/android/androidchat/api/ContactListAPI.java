package edu.galileo.android.androidchat.api;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import edu.galileo.android.androidchat.contactlist.ContactListEvent;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class ContactListAPI {
    private UserAPI userAPI;
    private Firebase dataReference;
    private ChildEventListener contactListEventListener;
    public final static String CONTACTS_PATH = "contacts";

    private static class SingletonHolder {
        private static final ContactListAPI INSTANCE = new ContactListAPI();
    }

    public static ContactListAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ContactListAPI(){
        APIHelper apiHelper = APIHelper.getInstance();
        dataReference = apiHelper.getDataReference();
        userAPI = UserAPI.getInstance();
    }

    public void notifyContactsOfConnectionChange(boolean online) {
        Firebase reference;
        User currentUser = userAPI.getCurrentUser();
        Map<String, Boolean> contacts = currentUser.getContacts();

        if (contacts != null) {
            for(Map.Entry<String, Boolean> entry : contacts.entrySet()) {
                String email = entry.getKey();
                reference = getOneContactReference(email, currentUser.getEmail());
                reference.setValue(online);
            }
        }
    }

    public void subscribeForContactListUpdates(){
        if (contactListEventListener == null) {
            contactListEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                    String email = dataSnapshot.getKey();
                    email = email.replace("_",".");
                    boolean online = ((Boolean)dataSnapshot.getValue()).booleanValue();
                    User user = new User(email, online, null);
                    postEvent(ContactListEvent.onContactAdded, user);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {
                    String email = dataSnapshot.getKey();
                    email = email.replace("_",".");
                    boolean online = ((Boolean)dataSnapshot.getValue()).booleanValue();
                    User user = new User(email, online, null);
                    postEvent(ContactListEvent.onContactChanged, user);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    String email = dataSnapshot.getKey();
                    email = email.replace("_",".");
                    boolean online = ((Boolean)dataSnapshot.getValue()).booleanValue();
                    User user = new User(email, online, null);
                    postEvent(ContactListEvent.onContactRemoved, user);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            };

            getMyContactsReference().addChildEventListener(contactListEventListener);
        }
    }

    public void destroyContactListListener() {
        contactListEventListener = null;
    }

    public void unSubscribeForContactListUpdates(){
        if (contactListEventListener != null) {
            dataReference.removeEventListener(contactListEventListener);
        }
    }

    public void removeContact(String email) {
        User currentUser = userAPI.getCurrentUser();
        String currentUserEmailKey = currentUser.getEmail().replace(".","_");
        String emailKey = email.replace(".","_");
        getOneContactReference(currentUserEmailKey, emailKey).removeValue();
        getOneContactReference(emailKey, currentUserEmailKey).removeValue();
    }

    public Firebase getMyContactsReference(){
        User currentUser = userAPI.getCurrentUser();
        String email = currentUser.getEmail();
        return getContactsReference(email);
    }

    public Firebase getContactsReference(String email){
        String key = email.replace(".","_");
        return userAPI.getUserReference(key).child(CONTACTS_PATH);
    }

    private Firebase getOneContactReference(String mainEmail, String childEmail){
        String mainKey = mainEmail.replace(".","_");
        String childKey = childEmail.replace(".","_");
        return userAPI.getUserReference(mainKey).child(CONTACTS_PATH).child(childKey);
    }

    private void postEvent(int type, User user) {
        ContactListEvent contactListEvent = new ContactListEvent();
        contactListEvent.setEventType(type);
        contactListEvent.setUser(user);
        EventBus eventBus = EventBus.getInstance();
        eventBus.post(contactListEvent);
    }
}
