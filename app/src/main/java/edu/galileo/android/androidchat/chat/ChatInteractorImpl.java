package edu.galileo.android.androidchat.chat;

import edu.galileo.android.androidchat.util.ChatUtil;

/**
 * Created by ykro.
 */
public class ChatInteractorImpl implements ChatInteractor {
    ChatUtil chatUtil;

    public ChatInteractorImpl() {
        this.chatUtil = ChatUtil.getInstance();
    }

    @Override
    public void subscribeForChatUpates() {
        chatUtil.subscribeForChatUpates();
    }

    @Override
    public void unSubscribeForChatUpates() {
        chatUtil.unSubscribeForChatUpates();
    }

    @Override
    public void destroyChatListener() {
        chatUtil.destroyChatListener();
    }

    @Override
    public void setRecipient(String recipient) {
        chatUtil.setReceiver(recipient);
    }

    @Override
    public void setSender() {
        chatUtil.setSender();
    }

    @Override
    public void sendMessage(String msg) {
        chatUtil.sendMessage(msg);
    }
}
