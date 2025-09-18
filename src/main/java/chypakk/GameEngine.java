package chypakk;

import chypakk.config.ConfigLoader;
import chypakk.config.GameConfig;
import chypakk.model.game.Castle;
import chypakk.model.game.GameState;
import chypakk.model.factory.GeneratorFactory;
import chypakk.ui.GameUI;
import chypakk.ui.LanternaUI;

import java.io.IOException;

//todo написать про паттерны в Obsidian
public class GameEngine {

    private final GameState castle;
    private final GameUI gameUI;
    private final GameConfig config;

    public GameEngine() {
        this.config = ConfigLoader.load();
        this.castle = new Castle(100, config);

        try {
            this.gameUI = new LanternaUI(castle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void start() {
        GeneratorFactory generatorFactory = new GeneratorFactory(config);
        for (var generatorConf : config.generators()){
            castle.getGeneratorManager().addGenerator(generatorFactory.createGenerator(generatorConf.type(), castle));
        }

        gameUI.start();
    }
}