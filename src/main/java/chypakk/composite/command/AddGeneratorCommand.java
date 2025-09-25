package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.GeneratorManagement;
import chypakk.model.managers.ResourceManagement;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

public class AddGeneratorCommand implements GameCommand {
    private final Logger logger = LoggerFactory.getLogger(AddGeneratorCommand.class);

    private final Supplier<ResourceGenerator> generatorFactory;
    private final Map<ResourceType, Integer> cost;
    private final GeneratorManagement generatorManagement;
    private final ResourceManagement resourceManagement;

    public AddGeneratorCommand(Supplier<ResourceGenerator> generatorFactory, Map<ResourceType, Integer> cost, GeneratorManagement generatorManagement, ResourceManagement resourceManagement) {
        this.generatorFactory = generatorFactory;
        this.cost = cost;
        this.generatorManagement = generatorManagement;
        this.resourceManagement = resourceManagement;
    }

    @Override
    public void execute(GameState castle) {
        if (resourceManagement.trySpendResources(cost)) {
            ResourceGenerator generator = generatorFactory.get();
            if (generator == null){
                logger.warn("Не удалось создать генератор: фабрика вернула null");
                castle.sendMessage("Ошибка: этот генератор недоступен в текущей версии игры.");

                resourceManagement.addResourceFromCost(cost);
                return;
            }

            generatorManagement.addGenerator(generator);
        }
    }
}
