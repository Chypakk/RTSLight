package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.resources.Wood;

public class ExchangeGoldCommand implements GameCommand {
    @Override
    public void execute(Castle castle) {
        if (!castle.haveBuilding("Рынок")){
            System.out.println("Не построен рынок!");
            return;
        }

        if (castle.getResource("gold") >= 5){

            castle.addResource(new Wood(10));
            castle.removeResource("gold", 5);

            System.out.println("Обмен совершен!");
        }
    }
}
