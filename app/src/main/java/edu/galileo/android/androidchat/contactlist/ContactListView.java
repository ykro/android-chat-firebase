package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public interface ContactListView {
    void onContactAdded(User user);
    void onContactChanged();
    void onContactRemoved();
    void onSubscriptionError();
}
