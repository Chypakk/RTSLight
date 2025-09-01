package chypakk.observer;

import chypakk.observer.event.GameEvent;

//наблюдатель
public interface GameObserver {
    void onMessage(String message);
    void onEvent(GameEvent gameEvent);
}
