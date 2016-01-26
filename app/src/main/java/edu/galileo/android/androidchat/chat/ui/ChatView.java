package edu.galileo.android.androidchat.chat.ui;

import edu.galileo.android.androidchat.chat.entities.ChatMessage;

/**
 * Created by ykro.
 */
public interface ChatView {
    void sendMessage();
    void onMessageReceived(ChatMessage msg);
}
