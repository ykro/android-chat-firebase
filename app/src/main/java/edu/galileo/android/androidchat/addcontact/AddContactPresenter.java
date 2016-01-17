package edu.galileo.android.androidchat.addcontact;

/**
 * Created by ykro.
 */
public interface AddContactPresenter {
    void onPause();
    void onResume();
    void onDestroy();

    void addContact(String email);
    void onEvent(AddContactEvent event);
}

