package chypakk.composite.command;

import chypakk.model.Castle;
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
    public void execute(Castle castle) {
        if (castle.haveBuilding(building)) return;

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

        castle.addBuilding(building);
    }
}
