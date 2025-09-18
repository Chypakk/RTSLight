package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.GeneratorManagement;

public class ShowGeneratorsCommand implements GameCommand{
    private final GeneratorManagement generatorManagement;

    public ShowGeneratorsCommand(GeneratorManagement generatorManagement) {
        this.generatorManagement = generatorManagement;
    }

    @Override
    public void execute(GameState castle) {
        generatorManagement.printGenerators();
    }
}
