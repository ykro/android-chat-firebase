package edu.galileo.android.androidchat.chat;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import edu.galileo.android.androidchat.repositories.RepositoryHelper;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.entities.ChatMessage;

/**
 * Created by ykro.
 */
public class ChatRepository {
    private Firebase dataReference;
    private RepositoryHelper repositoryHelper;
    private ChildEventListener chatEventListener;

    private final static String SEPARATOR = "___";
    private final static String CHATS_PATH = "chats";

    private String sender, receiver;

    private static class SingletonHolder {
        private static final ChatRepository INSTANCE = new ChatRepository();
    }
    public static ChatRepository getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ChatRepository(){
        repositoryHelper = RepositoryHelper.getInstance();
        dataReference = repositoryHelper.getDataReference();
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender() {
        this.sender = repositoryHelper.getAuthUserEmail();
    }

    public void subscribeForChatUpates() {
        if (chatEventListener == null) {

            chatEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    String msgSender = chatMessage.getSender();
                    msgSender = msgSender.replace("_",".");
                    chatMessage.setSentByMe(msgSender.equals(sender));

                    ChatEvent chatEvent = new ChatEvent(chatMessage);
                    EventBus eventBus = EventBus.getInstance();
                    eventBus.post(chatEvent);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            };
            getChatsReference().addChildEventListener(chatEventListener);
        }
    }

    public void unSubscribeForChatUpates() {
        if (chatEventListener != null) {
            dataReference.removeEventListener(chatEventListener);
        }
    }

    public void destroyChatListener() {
        chatEventListener = null;
    }

    public void sendMessage(String msg) {
        String keySender = this.sender.replace(".","_");
        ChatMessage chatMessage = new ChatMessage(keySender, msg);
        getChatsReference().push().setValue(chatMessage);
    }

    private Firebase getChatsReference(){
        String keySender = this.sender.replace(".","_");
        String keyReceiver = this.receiver.replace(".","_");

        String keyChat = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyReceiver) > 0) {
            keyChat = keyReceiver + SEPARATOR + keySender;
        }
        return dataReference.getRoot().child(CHATS_PATH).child(keyChat);
    }
}
