package chypakk.model.resources.generator;

import chypakk.model.game.GameState;
import chypakk.model.resources.Gold;
import chypakk.model.resources.Resource;

public class GoldMine extends ResourceGenerator{

    public GoldMine(int interval, int amountPerInterval, int totalAmount, GameState castle) {
        super(interval, amountPerInterval, totalAmount, castle);
    }

    @Override
    protected Resource createResource() {
        return new Gold(amountPerInterval);
    }
}