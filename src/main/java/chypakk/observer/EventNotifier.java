package chypakk.observer;

import chypakk.observer.event.GameEvent;

public interface EventNotifier {
    void notifyObservers(GameEvent event);
}
