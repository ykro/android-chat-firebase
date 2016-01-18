package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.api.ContactListAPI;
import edu.galileo.android.androidchat.api.UserAPI;

/**
 * Created by ykro.
 */
public class ContactListInteractorImpl implements ContactListInteractor {
    UserAPI userAPI;
    ContactListAPI contactListAPI;
    ContactListTaskFinishedListener listener;

    public ContactListInteractorImpl(ContactListTaskFinishedListener listener) {
        this.listener = listener;
        this.userAPI = UserAPI.getInstance();
        this.contactListAPI = ContactListAPI.getInstance();
    }

    @Override
    public void signOff() {
        userAPI.signOff();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        userAPI.changeUserConnectionStatus(online);
    }

    @Override
    public void subscribeForContactEvents() {
        contactListAPI.subscribeForContactListUpdates(listener);
    }

    @Override
    public void unSubscribeForContactEvents() {
        contactListAPI.unSubscribeForContactListUpdates();
    }

    @Override
    public void destroyContactListListener() {
        contactListAPI.destroyContactListListener();
    }

    @Override
    public void removeContact(String email) {
        contactListAPI.removeContact(email);
    }
}
