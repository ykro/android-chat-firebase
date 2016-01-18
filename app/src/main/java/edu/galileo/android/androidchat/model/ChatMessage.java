package edu.galileo.android.androidchat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by ykro.
 */

@JsonIgnoreProperties({"sentByMe"})
public class ChatMessage {
    String msg;
    String sender;
    boolean sentByMe;

    public ChatMessage(){}

    public ChatMessage(String sender, String msg){
        this.msg = msg;
        this.sender = sender;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        this.sentByMe = sentByMe;
    }
}
