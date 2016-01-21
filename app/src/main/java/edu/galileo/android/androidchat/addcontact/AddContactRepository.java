package edu.galileo.android.androidchat.addcontact;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import edu.galileo.android.androidchat.contactlist.ContactListRepository;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.entities.User;
import edu.galileo.android.androidchat.login.UserRepository;

/**
 * Created by ykro.
 */
public class AddContactRepository {
    private static class SingletonHolder {
        private static final AddContactRepository INSTANCE = new AddContactRepository();
    }
    public static AddContactRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public AddContactRepository(){
    }

    public void addContact(final String email) {
        final String key = email.replace(".","_");
        final UserRepository userRepository = UserRepository.getInstance();
        final Firebase userReference = userRepository.getUserReference(email);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                AddContactEvent event = new AddContactEvent();
                if (user != null) {
                    boolean online = user.isOnline();

                    ContactListRepository contactListRepository = ContactListRepository.getInstance();
                    Firebase userContactsReference = contactListRepository.getMyContactsReference();
                    userContactsReference.child(key).setValue(online);

                    User currentUser = userRepository.getCurrentUser();
                    String currentUserEmailKey = currentUser.getEmail();
                    currentUserEmailKey = currentUserEmailKey.replace(".","_");
                    Firebase reverseUserContactsReference = contactListRepository.getContactsReference(email);
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
