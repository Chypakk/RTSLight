package chypakk.ui;

import chypakk.observer.GameObserver;

public interface GameUI extends GameObserver, MenuRender {
    void initialize();
    void shutdown();
}
