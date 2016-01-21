package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.login.UserRepository;

/**
 * Created by ykro.
 */
public class ContactListInteractorImpl implements ContactListInteractor {
    UserRepository userRepository;
    ContactListRepository contactListRepository;

    public ContactListInteractorImpl() {
        this.userRepository = UserRepository.getInstance();
        this.contactListRepository = ContactListRepository.getInstance();
    }

    @Override
    public void signOff() {
        userRepository.signOff();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        userRepository.changeUserConnectionStatus(online);
    }

    @Override
    public void subscribeForContactEvents() {
        contactListRepository.subscribeForContactListUpdates();
    }

    @Override
    public void unSubscribeForContactEvents() {
        contactListRepository.unSubscribeForContactListUpdates();
    }

    @Override
    public void destroyContactListListener() {
        contactListRepository.destroyContactListListener();
    }

    @Override
    public void removeContact(String email) {
        contactListRepository.removeContact(email);
    }
}
