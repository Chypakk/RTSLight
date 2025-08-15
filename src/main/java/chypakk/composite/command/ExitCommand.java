package chypakk.composite.command;

import chypakk.model.Castle;

public class ExitCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        System.out.println("Выход из игры...");
        castle.stopAllGenerators();

        System.exit(0);
    }
}
