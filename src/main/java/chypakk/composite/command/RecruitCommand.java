package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.resources.ResourceType;
import chypakk.model.units.Unit;

import java.util.Map;

public class RecruitCommand implements GameCommand{

    private final Unit unit;
    private final Map<ResourceType, Integer> cost;

    public RecruitCommand(Unit unit, Map<ResourceType, Integer> cost) {
        this.unit = unit;
        this.cost = cost;
    }


    @Override
    public void execute(Castle castle) {
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

        castle.addUnit(unit);
    }
}
