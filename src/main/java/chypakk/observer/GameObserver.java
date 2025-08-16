package chypakk.observer;

import chypakk.model.GameState;
import chypakk.observer.event.GameEvent;

//наблюдатель
public interface GameObserver {
    void onGameStateChanged(GameState state);
    void onMessage(String message);
    void onEvent(GameEvent gameEvent);
}
