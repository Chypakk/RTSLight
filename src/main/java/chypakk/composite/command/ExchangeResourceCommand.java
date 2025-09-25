package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.BuildingManagement;
import chypakk.model.managers.ResourceManagement;
import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.ResourcesBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeResourceCommand implements GameCommand{
    private final Logger logger = LoggerFactory.getLogger(ExchangeResourceCommand.class);

    private final ResourceType resourceFrom;
    private final int amountFrom;
    private final ResourceType resourceTo;
    private final int amountTo;

    private final BuildingManagement buildingManagement;
    private final ResourceManagement resourceManagement;

    public ExchangeResourceCommand(ResourceType resourceFrom, int amountFrom, ResourceType resourceTo, int amountTo, BuildingManagement buildingManagement, ResourceManagement resourceManagement) {
        this.resourceFrom = resourceFrom;
        this.amountFrom = amountFrom;
        this.resourceTo = resourceTo;
        this.amountTo = amountTo;
        this.buildingManagement = buildingManagement;
        this.resourceManagement = resourceManagement;
    }

    @Override
    public void execute(GameState castle) {
        if (!buildingManagement.haveBuilding("Рынок")) {
            logger.info("не построен рынок");
            castle.sendMessage("Не построен рынок!");
            return;
        }

        Resource resourceAdd = ResourcesBuilder.generate(resourceTo, amountTo);

        if (resourceManagement.getResource(resourceFrom) >= amountFrom && resourceAdd != null) {
            resourceManagement.removeResource(resourceFrom, amountFrom);
            resourceManagement.addResource(resourceAdd);

            logger.info("обмен {} {} -> {} {}", amountFrom, resourceFrom, amountTo, resourceTo);

            castle.sendMessage(String.format("Обмен: %d %s → %d %s%n", amountFrom, resourceFrom, amountTo, resourceTo));
        } else if (resourceManagement.getResource(resourceFrom) < amountFrom){
            logger.info("недостаточно {}", resourceFrom);
            castle.sendMessage("Недостаточно " + resourceFrom);
        }
    }
}
