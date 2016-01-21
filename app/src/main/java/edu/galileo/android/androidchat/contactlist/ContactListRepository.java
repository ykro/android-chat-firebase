package edu.galileo.android.androidchat.contactlist;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import edu.galileo.android.androidchat.repositories.RepositoryHelper;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.entities.User;
import edu.galileo.android.androidchat.login.UserRepository;

/**
 * Created by ykro.
 */
public class ContactListRepository {
    private UserRepository userRepository;
    private Firebase dataReference;
    private ChildEventListener contactListEventListener;
    public final static String CONTACTS_PATH = "contacts";

    private static class SingletonHolder {
        private static final ContactListRepository INSTANCE = new ContactListRepository();
    }

    public static ContactListRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ContactListRepository(){
        RepositoryHelper repositoryHelper = RepositoryHelper.getInstance();
        dataReference = repositoryHelper.getDataReference();
        userRepository = UserRepository.getInstance();
    }

    public void notifyContactsOfConnectionChange(boolean online) {
        Firebase reference;
        User currentUser = userRepository.getCurrentUser();
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
        User currentUser = userRepository.getCurrentUser();
        String currentUserEmailKey = currentUser.getEmail().replace(".","_");
        String emailKey = email.replace(".","_");
        getOneContactReference(currentUserEmailKey, emailKey).removeValue();
        getOneContactReference(emailKey, currentUserEmailKey).removeValue();
    }

    public Firebase getMyContactsReference(){
        User currentUser = userRepository.getCurrentUser();
        String email = currentUser.getEmail();
        return getContactsReference(email);
    }

    public Firebase getContactsReference(String email){
        String key = email.replace(".","_");
        return userRepository.getUserReference(key).child(CONTACTS_PATH);
    }

    private Firebase getOneContactReference(String mainEmail, String childEmail){
        String mainKey = mainEmail.replace(".","_");
        String childKey = childEmail.replace(".","_");
        return userRepository.getUserReference(mainKey).child(CONTACTS_PATH).child(childKey);
    }

    private void postEvent(int type, User user) {
        ContactListEvent contactListEvent = new ContactListEvent();
        contactListEvent.setEventType(type);
        contactListEvent.setUser(user);
        EventBus eventBus = EventBus.getInstance();
        eventBus.post(contactListEvent);
    }
}
