package chypakk.composite.command;

import chypakk.model.game.GameState;

public class ExitCommand implements GameCommand{
    @Override
    public void execute(GameState castle) {
        System.out.println("Выход из игры...");
        castle.stopAllGenerators();

        System.exit(0);
    }
}
