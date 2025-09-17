package chypakk.composite.command.deprecated;

import chypakk.composite.command.GameCommand;
import chypakk.model.Castle;
import chypakk.model.GameState;
import chypakk.model.building.Marketplace;

import static chypakk.model.resources.ResourceType.GOLD;
import static chypakk.model.resources.ResourceType.WOOD;

@Deprecated
public class AddMarketplaceCommand implements GameCommand {

    @Override
    public void execute(GameState castle) {

        if (castle.haveBuilding("Рынок")){
            castle.sendMessage("Рынок уже построен!");
            return;
        }

        if ( castle.getResource(GOLD) >= 10 && castle.getResource(WOOD) >= 20){
            castle.removeResource(GOLD, 10);
            castle.removeResource(WOOD, 20);

            Marketplace marketplace = new Marketplace();
            castle.addBuilding(marketplace);
            castle.sendMessage("Рынок построен!");
        }
    }
}
