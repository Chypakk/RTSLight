package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.building.Building;
import chypakk.model.managers.BuildingManagement;
import chypakk.model.managers.BuildingManager;
import chypakk.model.managers.ResourceManagement;
import chypakk.model.managers.ResourceManager;
import chypakk.model.resources.ResourceType;

import java.util.Map;

public class AddBuildingCommand implements GameCommand {

    private final Building building;
    private final Map<ResourceType, Integer> cost;
    private final BuildingManagement buildingManager;
    private final ResourceManagement resourceManager;

    public AddBuildingCommand(Building building, Map<ResourceType, Integer> cost, BuildingManagement buildingManager, ResourceManagement resourceManager) {
        this.building = building;
        this.cost = cost;
        this.buildingManager = buildingManager;
        this.resourceManager = resourceManager;
    }

    @Override
    public void execute(GameState castle) {
        if (buildingManager.haveBuilding(building)) return;

        if (resourceManager.trySpendResources(cost)) {
            buildingManager.addBuilding(building);
        }
    }
}
