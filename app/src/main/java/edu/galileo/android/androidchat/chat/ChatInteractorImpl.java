package edu.galileo.android.androidchat.chat;

import edu.galileo.android.androidchat.api.ChatAPI;
import edu.galileo.android.androidchat.api.UserAPI;

/**
 * Created by ykro.
 */
public class ChatInteractorImpl implements ChatInteractor {
    ChatAPI chatAPI;
    UserAPI userAPI;

    public ChatInteractorImpl() {
        this.chatAPI = ChatAPI.getInstance();
        this.userAPI = UserAPI.getInstance();
    }

    @Override
    public void subscribeForChatUpates() {
        chatAPI.subscribeForChatUpates();
    }

    @Override
    public void unSubscribeForChatUpates() {
        chatAPI.unSubscribeForChatUpates();
    }

    @Override
    public void destroyChatListener() {
        chatAPI.destroyChatListener();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        userAPI.changeUserConnectionStatus(online);
    }

    @Override
    public void setRecipient(String recipient) {
        chatAPI.setReceiver(recipient);
    }

    @Override
    public void setSender() {
        chatAPI.setSender();
    }

    @Override
    public void sendMessage(String msg) {
        chatAPI.sendMessage(msg);
    }
}
