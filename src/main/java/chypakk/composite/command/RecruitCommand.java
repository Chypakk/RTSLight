package chypakk.composite.command;

import chypakk.model.game.GameState;
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
    public void execute(GameState castle) {
        if (castle.checkCostAndRemoveResources(cost)) return;

        castle.addUnit(unit);
    }
}
