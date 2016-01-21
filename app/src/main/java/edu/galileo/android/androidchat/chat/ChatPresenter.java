package edu.galileo.android.androidchat.chat;

/**
 * Created by ykro.
 */
public interface ChatPresenter {
    void onPause();
    void onResume();
    void onDestroy();

    void setChatSender();
    void setChatRecipient(String recipient);

    void sendMessage(String msg);
    void onEventMainThread(ChatEvent event);


}
