package edu.galileo.android.androidchat.lib;

/**
 * Created by ykro.
 */
public class EventBus {
    de.greenrobot.event.EventBus eventBus;

    private static class SingletonHolder {
        private static final EventBus INSTANCE = new EventBus();
    }

    public static EventBus getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public EventBus(){
        eventBus = de.greenrobot.event.EventBus.getDefault();
    }

    public void isRegistered(Object subscriber){
        eventBus.isRegistered(subscriber);
    }

    public void register(Object subscriber){
        eventBus.register(subscriber);
    }

    public void unregister(Object subscriber){
        eventBus.unregister(subscriber);
    }

    public void post(Object event){
        eventBus.post(event);
    }
}
