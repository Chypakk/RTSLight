package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.resources.generator.GoldMine;
import chypakk.model.resources.generator.ResourceGenerator;

import static chypakk.model.resources.ResourceType.GOLD;
import static chypakk.model.resources.ResourceType.WOOD;

public class AddGoldMineCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        if (castle.getResource(GOLD) >= 50 && castle.getResource(WOOD) >= 60) {

            castle.removeResource(GOLD, 50);
            castle.removeResource(WOOD, 60);
            ResourceGenerator goldMine = new GoldMine(2, 10, 70, castle);
            castle.addGenerator(goldMine);

        } else {
            System.out.println("Недостаточно ресурсов");
        }
    }
}
