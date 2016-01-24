package edu.galileo.android.androidchat.contactlist;

/**
 * Created by ykro.
 */
public class ContactListInteractorImpl implements ContactListInteractor {
    ContactListRepositoryImpl contactListRepository;

    public ContactListInteractorImpl() {
        this.contactListRepository = new ContactListRepositoryImpl();
    }

    @Override
    public void signOff() {
        contactListRepository.signOff();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        contactListRepository.changeUserConnectionStatus(online);
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
