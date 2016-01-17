package edu.galileo.android.androidchat.chat;

/**
 * Created by ykro.
 */
public interface ChatInteractor {
    void sendMessage(String msg);

    void subscribeForChatUpates();
    void unSubscribeForChatUpates();
    void destroyChatListener();

    void setSender();
    void setRecipient(String recipient);
}
