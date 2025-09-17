package chypakk.composite.command;

import chypakk.model.GameState;

public interface GameCommand {
    void execute(GameState gameState);
}
