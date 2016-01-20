package edu.galileo.android.androidchat.api;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.galileo.android.androidchat.events.AddContactEvent;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class AddContactAPI {
    private static class SingletonHolder {
        private static final AddContactAPI INSTANCE = new AddContactAPI();
    }
    public static AddContactAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public AddContactAPI(){
    }

    public void addContact(final String email) {
        final String key = email.replace(".","_");
        final UserAPI userAPI = UserAPI.getInstance();
        final Firebase userReference = userAPI.getUserReference(email);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                AddContactEvent event = new AddContactEvent();
                if (user != null) {
                    boolean online = user.isOnline();

                    ContactListAPI contactListAPI = ContactListAPI.getInstance();
                    Firebase userContactsReference = contactListAPI.getMyContactsReference();
                    userContactsReference.child(key).setValue(online);

                    User currentUser = userAPI.getCurrentUser();
                    String currentUserEmailKey = currentUser.getEmail();
                    currentUserEmailKey = currentUserEmailKey.replace(".","_");
                    Firebase reverseUserContactsReference = contactListAPI.getContactsReference(email);
                    reverseUserContactsReference.child(currentUserEmailKey).setValue(true);
                } else {
                    event.setError(true);
                }
                EventBus eventBus = EventBus.getInstance();
                eventBus.post(event);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });



    }
}
