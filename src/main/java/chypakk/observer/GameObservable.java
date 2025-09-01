package chypakk.observer;

import chypakk.observer.event.GameEvent;

//наблюдаемый
public interface GameObservable {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyObservers(GameEvent event);
}
