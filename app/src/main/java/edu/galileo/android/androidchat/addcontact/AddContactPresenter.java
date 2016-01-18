package edu.galileo.android.androidchat.addcontact;

/**
 * Created by ykro.
 */
public interface AddContactPresenter {
    void onShow();
    void onCancel();
    void onDestroy();

    void addContact(String email);
    void onEvent(AddContactEvent event);
}

