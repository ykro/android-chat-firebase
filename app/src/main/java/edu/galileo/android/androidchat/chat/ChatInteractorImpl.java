package edu.galileo.android.androidchat.chat;

import edu.galileo.android.androidchat.login.UserRepository;

/**
 * Created by ykro.
 */
public class ChatInteractorImpl implements ChatInteractor {
    ChatRepository chatRepository;
    UserRepository userRepository;

    public ChatInteractorImpl() {
        this.chatRepository = ChatRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    @Override
    public void subscribeForChatUpates() {
        chatRepository.subscribeForChatUpates();
    }

    @Override
    public void unSubscribeForChatUpates() {
        chatRepository.unSubscribeForChatUpates();
    }

    @Override
    public void destroyChatListener() {
        chatRepository.destroyChatListener();
    }

    @Override
    public void changeConnectionStatus(boolean online) {
        userRepository.changeUserConnectionStatus(online);
    }

    @Override
    public void setRecipient(String recipient) {
        chatRepository.setReceiver(recipient);
    }

    @Override
    public void setSender() {
        chatRepository.setSender();
    }

    @Override
    public void sendMessage(String msg) {
        chatRepository.sendMessage(msg);
    }
}
