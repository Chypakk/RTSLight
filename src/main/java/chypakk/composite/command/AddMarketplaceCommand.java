package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.building.Marketplace;

import static chypakk.model.resources.ResourceType.GOLD;
import static chypakk.model.resources.ResourceType.WOOD;

public class AddMarketplaceCommand implements GameCommand{

    @Override
    public void execute(Castle castle) {

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
