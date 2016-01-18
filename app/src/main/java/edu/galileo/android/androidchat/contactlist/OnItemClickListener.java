package edu.galileo.android.androidchat.contactlist;

import edu.galileo.android.androidchat.model.User;

/**
 * Created by ykro.
 */
public interface OnItemClickListener {
    void onItemClick(User user);
    void onItemLongClick(User user);
}
