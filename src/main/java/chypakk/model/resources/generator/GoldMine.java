package chypakk.model.resources.generator;

import chypakk.model.Castle;
import chypakk.model.resources.Gold;
import chypakk.observer.event.Action;
import chypakk.observer.event.GeneratorEvent;

import java.util.concurrent.TimeUnit;

public class GoldMine extends ResourceGenerator{

    public GoldMine(int interval, int amountPerInterval, int totalAmount, Castle castle) {
        super(interval, amountPerInterval, totalAmount, castle);
    }

    @Override
    public void startGenerator() {
        executor.scheduleAtFixedRate(() -> {

            castle.addResource(new Gold(amountPerInterval));
            totalAmount.addAndGet(-amountPerInterval);

            if (totalAmount.get() == amountPerInterval * 3){
                status = Status.ALMOST_REMOVED;
                castle.notifyObservers(new GeneratorEvent(
                        "GoldMine", Action.ALMOST_REMOVED
                ));

                castle.sendMessage("Золотая шахта скоро иссякнет");
            }

            if (totalAmount.get() <= 0){
                castle.sendMessage("Золотая шахта иссякла");
                stopGenerator();
            }

        }, 0, interval, TimeUnit.SECONDS);
    }
}