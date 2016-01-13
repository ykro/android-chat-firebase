package edu.galileo.android.androidchat.entities;

import java.util.Date;

/**
 * Created by ykro.
 */

public class ChatMessage {
    String msg;
    User sender;
    Date timestamp;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
