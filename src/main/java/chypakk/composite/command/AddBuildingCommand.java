package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.building.Building;
import chypakk.model.managers.BuildingManagement;
import chypakk.model.managers.ResourceManagement;
import chypakk.model.resources.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AddBuildingCommand implements GameCommand {

    private final Logger logger = LoggerFactory.getLogger(AddBuildingCommand.class);

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
        if (building == null) {
            logger.error("неизвестное здание");
            return;
        }

        if (buildingManager.haveBuilding(building)) {
            logger.debug("здание {} уже есть", building);
            return;
        }

        if (resourceManager.trySpendResources(cost)) {
            logger.info("Построено здание: {}", building.getName());
            buildingManager.addBuilding(building);
        } else {
            logger.warn("Недостаточно ресурсов для постройки здания: {}", building.getName());
        }
    }
}
