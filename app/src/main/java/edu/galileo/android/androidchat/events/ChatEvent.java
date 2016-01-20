package edu.galileo.android.androidchat.events;

import edu.galileo.android.androidchat.entities.ChatMessage;

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
