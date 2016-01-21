package edu.galileo.android.androidchat.addcontact;

/**
 * Created by ykro.
 */
public class AddContactInteractorImpl implements AddContactInteractor {
    AddContactRepository addContactRepository;

    public AddContactInteractorImpl() {
        this.addContactRepository = AddContactRepository.getInstance();
    }

    @Override
    public void addContact(String email) {
        addContactRepository.addContact(email);
    }
}
