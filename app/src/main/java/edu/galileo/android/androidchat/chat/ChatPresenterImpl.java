package edu.galileo.android.androidchat.chat;

import de.greenrobot.event.EventBus;
import edu.galileo.android.androidchat.entities.ChatMessage;

/**
 * Created by ykro.
 */
public class ChatPresenterImpl implements ChatPresenter {
    ChatView chatView;
    ChatInteractor chatInteractor;

    public ChatPresenterImpl(ChatView chatView){
        this.chatView = chatView;
        this.chatInteractor = new ChatInteractorImpl();
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        chatInteractor.subscribeForChatUpates();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        chatInteractor.unSubscribeForChatUpates();
    }

    @Override
    public void onDestroy() {
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
    public void onEvent(ChatMessageEvent event) {
        if (chatView != null) {
            ChatMessage msg = event.getMessage();
            chatView.onMessageReceived(msg);
        }
    }

}
