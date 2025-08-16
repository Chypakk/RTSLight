package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.ResourceGenerator;

import static chypakk.model.resources.ResourceType.WOOD;

public class AddForestCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        if (castle.getResource(WOOD) >= 70) {
            castle.removeResource(WOOD, 70);
            ResourceGenerator forest = new Forest(1, 5, 100, castle);
            castle.addGenerator(forest);
        } else {
            castle.sendMessage("Недостаточно ресурсов");
            //System.out.println("");
        }
    }
}
