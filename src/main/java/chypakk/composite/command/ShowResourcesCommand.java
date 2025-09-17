package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.GameState;

public class ShowResourcesCommand implements GameCommand{
    @Override
    public void execute(GameState castle) {
        castle.printResources();
    }
}
