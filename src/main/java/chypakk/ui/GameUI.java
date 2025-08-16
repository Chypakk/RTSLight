package chypakk.ui;

import chypakk.model.Castle;
import chypakk.model.GameState;
import chypakk.observer.GameObserver;
import chypakk.observer.event.GameEvent;
import chypakk.observer.event.GeneratorEvent;

public abstract class GameUI implements GameObserver {
    protected final Castle castle;

    public GameUI(Castle castle) {
        this.castle = castle;
        castle.addObserver(this);
    }

    public abstract void start();

    public abstract void render(GameState state);

    @Override
    public void onGameStateChanged(GameState state) {
        render(state);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("[СООБЩЕНИЕ] " + message);
    }

    @Override
    public void onEvent(GameEvent gameEvent) {
        switch (gameEvent) {
            case GeneratorEvent event -> handleGeneratorEvent(event);

            default -> throw new IllegalStateException("Unexpected value: " + gameEvent);
        }
    }

    private void handleGeneratorEvent(GeneratorEvent event) {
        System.out.println("[СООБЩЕНИЕ] " + event.getAction() + " " + event.getGeneratorType());
    }
}
