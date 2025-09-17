package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.building.Building;
import chypakk.model.resources.ResourceType;

import java.util.Map;

public class AddBuildingCommand implements GameCommand {

    private final Building building;
    private final Map<ResourceType, Integer> cost;

    public AddBuildingCommand(Building building, Map<ResourceType, Integer> cost) {
        this.building = building;
        this.cost = cost;
    }

    @Override
    public void execute(GameState castle) {
        if (castle.haveBuilding(building)) return;
        if (castle.checkCostAndRemoveResources(cost)) return;

        castle.addBuilding(building);
    }
}
