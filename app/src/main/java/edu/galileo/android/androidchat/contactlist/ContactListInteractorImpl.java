package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.util.ContactListUtil;
import edu.galileo.android.androidchat.util.LoginUtil;

/**
 * Created by ykro.
 */
public class ContactListInteractorImpl implements ContactListInteractor {
    LoginUtil loginUtil;
    ContactListUtil contactListUtil;
    ContactListTaskFinishedListener listener;

    public ContactListInteractorImpl(ContactListTaskFinishedListener listener) {
        this.listener = listener;
        this.loginUtil = LoginUtil.getInstance();
        this.contactListUtil = ContactListUtil.getInstance();
    }

    @Override
    public void signOff() {
        loginUtil.signOff();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        loginUtil.changeUserConnectionStatus(online);
    }

    @Override
    public void subscribeForContactEvents() {
        contactListUtil.subscribeForContactListUpdates(listener);
    }

    @Override
    public void unSubscribeForContactEvents() {
        contactListUtil.unSubscribeForContactListUpdates();
    }

    @Override
    public void destroyContactListListener() {
        contactListUtil.destroyContactListListener();
    }

    @Override
    public void removeContact(String email) {
        contactListUtil.removeContact(email);
    }
}
