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
        if (castle.checkCostAndRemoveResources(cost)) return;

        ResourceGenerator generator = generatorFactory.get();
        castle.addGenerator(generator);
    }
}
