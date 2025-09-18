package chypakk.composite.command;

import chypakk.model.game.GameState;
import chypakk.model.managers.ResourceManagement;

public class ShowResourcesCommand implements GameCommand{
    private final ResourceManagement resourceManagement;

    public ShowResourcesCommand(ResourceManagement resourceManagement) {
        this.resourceManagement = resourceManagement;
    }

    @Override
    public void execute(GameState castle) {
        resourceManagement.printResources();
    }
}
