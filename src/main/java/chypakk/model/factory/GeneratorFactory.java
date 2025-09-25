package chypakk.model.factory;

import chypakk.config.GameConfig;
import chypakk.config.GeneratorConfig;
import chypakk.model.game.GameState;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.GoldMine;
import chypakk.model.resources.generator.ResourceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneratorFactory {
    private final Logger logger = LoggerFactory.getLogger(GeneratorFactory.class);

    private final GameConfig config;

    public GeneratorFactory(GameConfig config) {
        this.config = config;
    }
    
    public ResourceGenerator createGenerator(String type, GameState castle){
        GeneratorConfig generatorConfig = getGeneratorConfig(type);

        return switch (type) {
            case "GoldMine" -> new GoldMine(
                    generatorConfig.interval(),
                    generatorConfig.amountPerInterval(),
                    generatorConfig.totalAmount(),
                    castle
            );
            case "Forest" -> new Forest(
                    generatorConfig.interval(),
                    generatorConfig.amountPerInterval(),
                    generatorConfig.totalAmount(),
                    castle
            );
            default -> {
                logger.error("Неизвестный тип генератора: {}", type);
                yield null;
            }
        };
    }

    private GeneratorConfig getGeneratorConfig(String type) {
        return config.generators().stream()
                .filter(g -> g.type().equals(type))
                .findFirst()
                .orElse(null);
    }
}
