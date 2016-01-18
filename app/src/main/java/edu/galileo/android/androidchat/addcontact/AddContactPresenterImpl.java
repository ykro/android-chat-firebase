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
    public void onShow() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        addContactView = null;
        EventBus.getDefault().unregister(this);
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

            if (event.isError()) {
                addContactView.contactNotAdded();
            } else {
                addContactView.contactAdded();
            }
        }
    }
}
