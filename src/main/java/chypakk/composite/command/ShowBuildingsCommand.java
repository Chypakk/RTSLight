package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.BuildingManagement;

public class ShowBuildingsCommand implements GameCommand{
    private final BuildingManagement buildingManagement;

    public ShowBuildingsCommand(BuildingManagement buildingManagement) {
        this.buildingManagement = buildingManagement;
    }

    @Override
    public void execute(GameState castle) {
        buildingManagement.printBuildings();
    }
}
