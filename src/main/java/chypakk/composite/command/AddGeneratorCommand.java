package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;

import java.util.Map;
import java.util.function.Supplier;

public class AddGeneratorCommand implements GameCommand {

    private final Supplier<ResourceGenerator> generatorFactory;
    private final Map<ResourceType, Integer> cost;

    public AddGeneratorCommand(Supplier<ResourceGenerator> generatorFactory, Map<ResourceType, Integer> cost) {
        this.generatorFactory = generatorFactory;
        this.cost = cost;
    }

    @Override
    public void execute(GameState castle) {
        for (var entry : cost.entrySet()) {
            ResourceType type = entry.getKey();
            int required = entry.getValue();
            if (castle.getResource(type) < required) {
                castle.sendMessage("Недостаточно ресурса: " + type + ". Нужно: " + required);
                return;
            }
        }

        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            castle.removeResource(entry.getKey(), entry.getValue());
        }

        ResourceGenerator generator = generatorFactory.get();
        castle.addGenerator(generator);
    }
}
