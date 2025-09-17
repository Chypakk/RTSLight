package chypakk.composite.command;

import chypakk.model.game.GameState;

public class ShowGeneratorsCommand implements GameCommand{
    @Override
    public void execute(GameState castle) {
        castle.printGenerators();
    }
}
