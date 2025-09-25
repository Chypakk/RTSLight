package chypakk;

import chypakk.config.ConfigLoader;
import chypakk.config.GameConfig;
import chypakk.model.game.Castle;
import chypakk.model.game.GameState;
import chypakk.model.factory.GeneratorFactory;
import chypakk.ui.GameUI;
import chypakk.ui.LanternaUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//todo написать про паттерны в Obsidian
public class GameEngine {

    private final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private final GameState castle;
    private final GameUI gameUI;
    private final GameConfig config;

    public GameEngine() {
        this.config = ConfigLoader.load();
        this.castle = new Castle(100, config);

        try {
            this.gameUI = new LanternaUI(castle);
        } catch (IOException e) {
            logger.error("Не удалось инициализировать LanternaUI", e);
            throw new RuntimeException(e);
        }

    }

    public void start() {
        logger.info("Старт игры");

        GeneratorFactory generatorFactory = new GeneratorFactory(config);
        for (var generatorConf : config.generators()){
            if (generatorConf.initial()){
                logger.debug("создание {}", generatorConf);
                castle.getGeneratorManager().addGenerator(generatorFactory.createGenerator(generatorConf.type(), castle));
            }
        }

        logger.info("запуск ui");
        gameUI.start();
    }
}