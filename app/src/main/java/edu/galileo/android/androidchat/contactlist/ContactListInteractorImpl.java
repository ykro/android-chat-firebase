package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.util.BackendUtil;

/**
 * Created by ykro.
 */
public class ContactListInteractorImpl implements ContactListInteractor {
    BackendUtil backendUtil;
    ContactListTaskFinishedListener listener;

    public ContactListInteractorImpl(ContactListTaskFinishedListener listener) {
        this.listener = listener;
        this.backendUtil = new BackendUtil();
    }

    @Override
    public void signOff() {
        backendUtil.signOff();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        backendUtil.changeUserConnectionStatus(online);
    }

    @Override
    public void subscribeForContactEvents() {
        backendUtil.subscribeForContactListUpdates(listener);
    }

    @Override
    public void unSubscribeForContactEvents() {
        backendUtil.unSubscribeForContactListUpdates();
    }
}
