package edu.galileo.android.androidchat.addcontact;

import edu.galileo.android.androidchat.util.AddContactUtil;

/**
 * Created by ykro.
 */
public class AddContactInteractorImpl implements AddContactInteractor {
    AddContactUtil addContactUtil;

    public AddContactInteractorImpl() {
        this.addContactUtil = AddContactUtil.getInstance();
    }

    @Override
    public void addContact(String email) {
        addContactUtil.addContact(email);
    }
}
