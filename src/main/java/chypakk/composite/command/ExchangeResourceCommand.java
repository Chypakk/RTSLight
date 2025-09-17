package chypakk.composite.command;

import chypakk.model.Castle;
import chypakk.model.GameState;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.ResourcesBuilder;

public class ExchangeResourceCommand implements GameCommand{

    private final ResourceType resourceFrom;
    private final int amountFrom;
    private final ResourceType resourceTo;
    private final int amountTo;

    public ExchangeResourceCommand(ResourceType resourceFrom, int amountFrom, ResourceType resourceTo, int amountTo) {
        this.resourceFrom = resourceFrom;
        this.amountFrom = amountFrom;
        this.resourceTo = resourceTo;
        this.amountTo = amountTo;
    }

    @Override
    public void execute(GameState castle) {
        if (!castle.haveBuilding("Рынок")) {
            castle.sendMessage("Не построен рынок!");
            return;
        }

        if (castle.getResource(resourceFrom) >= amountFrom) {
            castle.removeResource(resourceFrom, amountFrom);
            castle.addResource(ResourcesBuilder.generate(resourceTo, amountTo));

            castle.sendMessage(String.format("Обмен: %d %s → %d %s%n", amountFrom, resourceFrom, amountTo, resourceTo));
        } else {
            castle.sendMessage("Недостаточно " + resourceFrom);
        }
    }
}
