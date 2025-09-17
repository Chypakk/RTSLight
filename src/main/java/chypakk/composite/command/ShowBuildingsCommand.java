package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.GameState;

public class ShowBuildingsCommand implements GameCommand{
    @Override
    public void execute(GameState castle) {
        castle.printBuildings();
    }
}
