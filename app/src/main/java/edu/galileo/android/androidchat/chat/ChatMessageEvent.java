package edu.galileo.android.androidchat.chat;

import edu.galileo.android.androidchat.entities.ChatMessage;

/**
 * Created by ykro.
 */
public class ChatMessageEvent {
    ChatMessage msg;

    public ChatMessageEvent(ChatMessage msg) {
        this.msg = msg;
    }

    public ChatMessage getMessage() {
        return msg;
    }
}
