package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.model.User;

/**
 * Created by ykro.
 */
public interface ContactListView {
    void onContactAdded(User user);
    void onContactChanged(User user);
    void onContactRemoved(User user);
}
