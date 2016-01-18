package edu.galileo.android.androidchat.addcontact;

import edu.galileo.android.androidchat.api.AddContactAPI;

/**
 * Created by ykro.
 */
public class AddContactInteractorImpl implements AddContactInteractor {
    AddContactAPI addContactAPI;

    public AddContactInteractorImpl() {
        this.addContactAPI = AddContactAPI.getInstance();
    }

    @Override
    public void addContact(String email) {
        addContactAPI.addContact(email);
    }
}
