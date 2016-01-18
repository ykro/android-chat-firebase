package edu.galileo.android.androidchat.util;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import de.greenrobot.event.EventBus;
import edu.galileo.android.androidchat.addcontact.AddContactEvent;
import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class AddContactUtil {
    private static class SingletonHolder {
        private static final AddContactUtil INSTANCE = new AddContactUtil();
    }
    public static AddContactUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public AddContactUtil(){
    }

    public void addContact(final String email) {
        final String key = email.replace(".","_");
        final LoginUtil loginUtil = LoginUtil.getInstance();
        final Firebase userReference = loginUtil.getUserReference(email);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                AddContactEvent event = new AddContactEvent();
                if (user != null) {
                    boolean online = user.isOnline();

                    ContactListUtil contactListUtil = ContactListUtil.getInstance();
                    Firebase userContactsReference = contactListUtil.getMyContactsReference();
                    userContactsReference.child(key).setValue(online);

                    User currentUser = loginUtil.getCurrentUser();
                    String currentUserEmailKey = currentUser.getEmail();
                    currentUserEmailKey = currentUserEmailKey.replace(".","_");
                    Firebase reverseUserContactsReference = contactListUtil.getContactsReference(email);
                    reverseUserContactsReference.child(currentUserEmailKey).setValue(true);
                } else {
                    event.setError(true);
                }
                EventBus.getDefault().post(event);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });



    }
}
