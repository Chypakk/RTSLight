package chypakk.composite.command;

import chypakk.model.game.GameState;

public class ShowBuildingsCommand implements GameCommand{
    @Override
    public void execute(GameState castle) {
        castle.printBuildings();
    }
}
