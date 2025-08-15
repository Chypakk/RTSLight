package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.resources.Gold;

public class ExchangeWoodCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        if (!castle.haveBuilding("Рынок")){
            System.out.println("Не построен рынок!");
            return;
        }

        if (castle.getResource("wood") >= 10){

            castle.addResource(new Gold(5));
            castle.removeResource("wood", 10);

            System.out.println("Обмен совершен!");
        }
    }
}
