package edu.galileo.android.androidchat.chat;

/**
 * Created by ykro.
 */
public class ChatMessageEvent {
    String msg;

    public ChatMessageEvent(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
