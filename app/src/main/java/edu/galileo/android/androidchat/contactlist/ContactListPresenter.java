package edu.galileo.android.androidchat.contactlist;

/**
 * Created by ykro.
 */
public interface ContactListPresenter {
    void onPause();
    void onResume();
    void onDestroy();

    void signOff();
    void removeContact(String email);
}
