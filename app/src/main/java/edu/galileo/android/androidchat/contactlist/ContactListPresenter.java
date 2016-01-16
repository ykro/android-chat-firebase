package edu.galileo.android.androidchat.contactlist;

/**
 * Created by ykro.
 */
public interface ContactListPresenter {
    void onStop();
    void onStart();
    void onDestroy();

    void signOff();
    void onItemClick(int position);
    void onItemLongClick(int position);
}
