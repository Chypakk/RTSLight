package chypakk.command;

import chypakk.model.Castle;

public class ShowGeneratorsCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        castle.printGenerators();
    }
}
