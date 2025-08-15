package chypakk.composite.command;

import chypakk.model.Castle;

public class ShowResourcesCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        castle.printResources();
    }
}
