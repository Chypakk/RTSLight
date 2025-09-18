package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.ResourceManagement;
import chypakk.model.managers.UnitManagement;
import chypakk.model.resources.ResourceType;
import chypakk.model.units.Unit;

import java.util.Map;

public class RecruitCommand implements GameCommand{

    private final Unit unit;
    private final Map<ResourceType, Integer> cost;
    private final UnitManagement unitManagement;
    private final ResourceManagement resourceManagement;

    public RecruitCommand(Unit unit, Map<ResourceType, Integer> cost, UnitManagement unitManagement, ResourceManagement resourceManagement) {
        this.unit = unit;
        this.cost = cost;
        this.unitManagement = unitManagement;
        this.resourceManagement = resourceManagement;
    }


    @Override
    public void execute(GameState castle) {
        if (resourceManagement.trySpendResources(cost)) {
            unitManagement.addUnit(unit);
        }
    }
}
