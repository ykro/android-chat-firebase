package edu.galileo.android.androidchat.chat;

import edu.galileo.android.androidchat.events.ChatEvent;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.entities.ChatMessage;
import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class ChatPresenterImpl implements ChatPresenter {
    EventBus eventBus;
    ChatView chatView;
    ChatInteractor chatInteractor;

    public ChatPresenterImpl(ChatView chatView){
        this.chatView = chatView;
        this.eventBus = EventBus.getInstance();
        this.chatInteractor = new ChatInteractorImpl();
    }

    @Override
    public void onResume() {
        eventBus.register(this);
        chatInteractor.subscribeForChatUpates();
        chatInteractor.changeConnectionStatus(User.ONLINE);
    }

    @Override
    public void onPause() {
        chatInteractor.unSubscribeForChatUpates();
        chatInteractor.changeConnectionStatus(User.OFFLINE);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        chatInteractor.destroyChatListener();
        chatView = null;
    }

    @Override
    public void setChatRecipient(String recipient) {
        this.chatInteractor.setRecipient(recipient);
    }

    @Override
    public void setChatSender() {
        this.chatInteractor.setSender();
    }

    @Override
    public void sendMessage(String msg) {
        chatInteractor.sendMessage(msg);
    }

    @Override
    public void onEvent(ChatEvent event) {
        if (chatView != null) {
            ChatMessage msg = event.getMessage();
            chatView.onMessageReceived(msg);
        }
    }

}
