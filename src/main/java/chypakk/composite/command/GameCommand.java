package chypakk.composite.command;

import chypakk.model.game.GameState;

public interface GameCommand {
    void execute(GameState gameState);
}
