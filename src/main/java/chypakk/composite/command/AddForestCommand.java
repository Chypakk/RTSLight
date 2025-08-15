package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.ResourceGenerator;

public class AddForestCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        if (castle.getResource("wood") >= 70) {
            castle.removeResource("wood", 70);
            ResourceGenerator forest = new Forest(1, 5, 100, castle);
            castle.addGenerator(forest);
        } else {
            System.out.println("Недостаточно ресурсов");
        }
    }
}
