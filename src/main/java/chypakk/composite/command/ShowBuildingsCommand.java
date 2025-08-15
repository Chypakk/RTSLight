package chypakk.composite.command;

import chypakk.model.Castle;

public class ShowBuildingsCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        castle.printBuildings();
    }
}
