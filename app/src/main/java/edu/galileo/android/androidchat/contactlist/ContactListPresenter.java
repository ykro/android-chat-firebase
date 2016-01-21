package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.events.ContactListEvent;

/**
 * Created by ykro.
 */
public interface ContactListPresenter {
    void onPause();
    void onResume();
    void onCreate();
    void onDestroy();

    void signOff();
    void removeContact(String email);
    void onEventMainThread(ContactListEvent event);
}
