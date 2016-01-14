package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class ContactListPresenterImpl implements ContactListPresenter,
                                                 ContactListTaskFinishedListener {
    ContactListView contactListView;
    ContactListInteractor contactListInteractor;

    public ContactListPresenterImpl(ContactListView contactListView){
        this.contactListView = contactListView;
        this.contactListInteractor = new ContactListInteractorImpl(this);
    }

    @Override
    public void signOff() {
        contactListInteractor.signOff();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onResume() {
        contactListInteractor.changeConnectionStatus(User.ONLINE);
        contactListInteractor.subscribeForContactEvents();
    }

    @Override
    public void onStop() {
        contactListInteractor.changeConnectionStatus(User.OFFLINE);
        contactListInteractor.unSubscribeForContactEvents();
    }

    @Override
    public void onDestroy() {
        contactListView = null;
    }

    @Override
    public void onSubscriptionError(String error) {
        contactListView.onSubscriptionError();
    }

    @Override
    public void onContactAdded(User user) {
        if (contactListView != null) {
            contactListView.onContactAdded(user);
        }
    }

    @Override
    public void onContactChanged() {
        if (contactListView != null) {
            contactListView.onContactChanged();
        }
    }

    @Override
    public void onContactRemoved() {
        if (contactListView != null) {
            contactListView.onContactRemoved();
        }
    }
}
