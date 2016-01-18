package edu.galileo.android.androidchat.addcontact;

/**
 * Created by ykro.
 */
public interface AddContactPresenter {
    void onShow();
    void onDestroy();

    void addContact(String email);
    void onEvent(AddContactEvent event);
}

