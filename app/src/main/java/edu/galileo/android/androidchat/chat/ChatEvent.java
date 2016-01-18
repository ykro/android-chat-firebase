package edu.galileo.android.androidchat.chat;

import edu.galileo.android.androidchat.model.ChatMessage;

/**
 * Created by ykro.
 */
public class ChatEvent {
    ChatMessage msg;

    public ChatEvent(ChatMessage msg) {
        this.msg = msg;
    }

    public ChatMessage getMessage() {
        return msg;
    }
}
