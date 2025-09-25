package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.GeneratorManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExitCommand implements GameCommand{
    private final Logger logger = LoggerFactory.getLogger(ExitCommand.class);
    private final GeneratorManagement generatorManager;

    public ExitCommand(GeneratorManagement generatorManager) {
        this.generatorManager = generatorManager;
    }

    @Override
    public void execute(GameState castle) {
        logger.info("Пользователь инициировал выход из игры");

        castle.sendMessage("Выход из игры...");
        generatorManager.stopAllGenerators();
        castle.setGameActive(false);
    }
}
