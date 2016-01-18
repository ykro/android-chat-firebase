package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.api.ContactListAPI;
import edu.galileo.android.androidchat.api.UserAPI;

/**
 * Created by ykro.
 */
public class ContactListInteractorImpl implements ContactListInteractor {
    UserAPI userAPI;
    ContactListAPI contactListAPI;

    public ContactListInteractorImpl() {
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
        contactListAPI.subscribeForContactListUpdates();
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
