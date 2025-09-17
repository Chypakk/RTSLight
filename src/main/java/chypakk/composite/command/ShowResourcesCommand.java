package chypakk.composite.command;

import chypakk.model.game.GameState;

public class ShowResourcesCommand implements GameCommand{
    @Override
    public void execute(GameState castle) {
        castle.printResources();
    }
}
