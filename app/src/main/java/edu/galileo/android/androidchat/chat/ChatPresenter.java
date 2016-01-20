package edu.galileo.android.androidchat.chat;

import edu.galileo.android.androidchat.events.ChatEvent;

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
    void onEvent(ChatEvent event);


}
