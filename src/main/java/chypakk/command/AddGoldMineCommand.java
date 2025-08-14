package chypakk.command;

import chypakk.model.Castle;
import chypakk.model.resources.generator.GoldMine;
import chypakk.model.resources.generator.ResourceGenerator;

public class AddGoldMineCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        if (castle.getResource("gold") >= 50 && castle.getResource("wood") >= 60) {

            castle.removeResource("gold", 50);
            castle.removeResource("wood", 60);
            ResourceGenerator goldMine = new GoldMine(2, 10, 70, castle);
            castle.addGenerator(goldMine);

        } else {
            System.out.println("Недостаточно ресурсов");
        }
    }
}
