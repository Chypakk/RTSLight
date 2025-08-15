package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.building.Marketplace;

public class AddMarketplaceCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {

        if (castle.haveBuilding("Рынок")){
            System.out.println("Рынок уже построен!");
            return;
        }

        if ( castle.getResource("gold") >= 10 && castle.getResource("wood") >= 20){
            castle.removeResource("gold", 10);
            castle.removeResource("wood", 20);

            Marketplace marketplace = new Marketplace();
            castle.addBuilding(marketplace);
            System.out.println("Рынок построен!");
        }
    }
}
