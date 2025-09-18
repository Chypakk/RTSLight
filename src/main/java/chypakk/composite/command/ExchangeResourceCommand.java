package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.BuildingManagement;
import chypakk.model.managers.ResourceManagement;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.ResourcesBuilder;

public class ExchangeResourceCommand implements GameCommand{

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
            castle.sendMessage("Не построен рынок!");
            return;
        }

        if (resourceManagement.getResource(resourceFrom) >= amountFrom) {
            resourceManagement.removeResource(resourceFrom, amountFrom);
            resourceManagement.addResource(ResourcesBuilder.generate(resourceTo, amountTo));

            castle.sendMessage(String.format("Обмен: %d %s → %d %s%n", amountFrom, resourceFrom, amountTo, resourceTo));
        } else {
            castle.sendMessage("Недостаточно " + resourceFrom);
        }
    }
}
