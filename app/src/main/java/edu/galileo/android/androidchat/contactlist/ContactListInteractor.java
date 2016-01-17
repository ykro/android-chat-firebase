package edu.galileo.android.androidchat.contactlist;

/**
 * Created by ykro.
 */
public interface ContactListInteractor {
    void signOff();
    void changeConnectionStatus(boolean online);
    void subscribeForContactEvents();
    void unSubscribeForContactEvents();
    void destroyContactListListener();

    void removeContact(String email);
}
