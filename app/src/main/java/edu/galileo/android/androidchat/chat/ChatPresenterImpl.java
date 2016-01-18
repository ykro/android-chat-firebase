package edu.galileo.android.androidchat.chat;

import de.greenrobot.event.EventBus;
import edu.galileo.android.androidchat.model.ChatMessage;
import edu.galileo.android.androidchat.model.User;

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
        chatInteractor.changeConnectionStatus(User.ONLINE);
    }

    @Override
    public void onPause() {
        chatInteractor.unSubscribeForChatUpates();
        chatInteractor.changeConnectionStatus(User.OFFLINE);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
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
