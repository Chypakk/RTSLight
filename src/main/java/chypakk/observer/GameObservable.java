package chypakk.observer;

//наблюдаемый
public interface GameObservable {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyObservers();
}
