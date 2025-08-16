package chypakk.observer;

import chypakk.model.GameState;

//наблюдатель
public interface GameObserver {
    void onGameStateChanged(GameState state);
    void onMessage(String message);
}
