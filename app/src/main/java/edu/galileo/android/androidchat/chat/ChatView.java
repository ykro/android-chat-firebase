package edu.galileo.android.androidchat.chat;

import edu.galileo.android.androidchat.model.ChatMessage;

/**
 * Created by ykro.
 */
public interface ChatView {
    void sendMessage();
    void onMessageReceived(ChatMessage msg);
}
