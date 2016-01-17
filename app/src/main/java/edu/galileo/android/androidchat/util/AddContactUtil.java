package edu.galileo.android.androidchat.util;

import com.firebase.client.Firebase;

import de.greenrobot.event.EventBus;
import edu.galileo.android.androidchat.addcontact.AddContactEvent;

/**
 * Created by ykro.
 */
public class AddContactUtil {
    private Firebase userContactsReference;

    private static class SingletonHolder {
        private static final AddContactUtil INSTANCE = new AddContactUtil();
    }
    public static AddContactUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public AddContactUtil(){
        ContactListUtil contactListUtil = ContactListUtil.getInstance();
        userContactsReference = contactListUtil.getContactsReference();
    }

    public void addContact(String email) {
        String key = email.replace(".","_");
        userContactsReference.child(key).setValue(false);
        //reisar si el usuario existe
        EventBus.getDefault().post(new AddContactEvent());
    }
}
