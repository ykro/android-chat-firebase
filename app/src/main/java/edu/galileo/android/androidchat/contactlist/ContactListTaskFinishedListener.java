package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public interface ContactListTaskFinishedListener {
    void onSubscriptionError(String error);

    void onContactAdded(User user);
    void onContactChanged();
    void onContactRemoved();
}
