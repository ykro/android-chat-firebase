package edu.galileo.android.androidchat.chat;

/**
 * Created by ykro.
 */
public interface ChatView {
    void showContent();
    void hideContent();
    void showProgress();
    void hideProgress();

    void sendMessage();
    void onMessageReceived(String msg);
}
