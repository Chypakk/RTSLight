package chypakk.model.factory;

import chypakk.config.GameConfig;
import chypakk.config.GeneratorConfig;
import chypakk.model.Castle;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.GoldMine;
import chypakk.model.resources.generator.ResourceGenerator;

public class GeneratorFactory {
    private final GameConfig config;

    public GeneratorFactory(GameConfig config) {
        this.config = config;
    }
    
    public ResourceGenerator createGenerator(String type, Castle castle){
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
            default -> throw new IllegalArgumentException("Неизвестный тип генератора: " + type);
        };
    }

    private GeneratorConfig getGeneratorConfig(String type) {
        return config.generators().stream()
                .filter(g -> g.type().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный тип генератора: " + type));
    }
}
