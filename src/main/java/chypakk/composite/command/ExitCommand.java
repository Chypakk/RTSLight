package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.GeneratorManagement;

public class ExitCommand implements GameCommand{
    private final GeneratorManagement generatorManager;

    public ExitCommand(GeneratorManagement generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public void execute(GameState castle) {
        System.out.println("Выход из игры...");
        generatorManager.stopAllGenerators();
        castle.setGameActive(false);
    }
}
