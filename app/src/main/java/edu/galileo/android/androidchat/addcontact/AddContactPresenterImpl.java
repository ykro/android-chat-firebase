package edu.galileo.android.androidchat.addcontact;

import de.greenrobot.event.EventBus;

/**
 * Created by ykro.
 */
public class AddContactPresenterImpl implements AddContactPresenter {
    AddContactView addContactView;
    AddContactInteractor addContactInteractor;

    public AddContactPresenterImpl(AddContactView addContactView) {
        this.addContactView = addContactView;
        this.addContactInteractor = new AddContactInteractorImpl();
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        addContactView = null;
    }

    @Override
    public void addContact(String email) {
        addContactView.hideInput();
        addContactView.showProgress();
        this.addContactInteractor.addContact(email);
    }

    @Override
    public void onEvent(AddContactEvent event) {
        if (addContactView != null) {
            addContactView.hideProgress();
            addContactView.showInput();

            if (event.getError() == null) {
                addContactView.contactAdded();
            } else {
                addContactView.contactNotAdded(event.getError());
            }
        }
    }
}
