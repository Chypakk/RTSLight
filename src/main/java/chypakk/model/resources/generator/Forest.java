package chypakk.model.resources.generator;

import chypakk.model.Castle;
import chypakk.model.resources.Wood;
import chypakk.observer.event.Action;
import chypakk.observer.event.GeneratorEvent;

import java.util.concurrent.TimeUnit;

public class Forest extends ResourceGenerator {

    public Forest(int interval, int amountPerInterval, int totalAmount, Castle castle) {
        super(interval, amountPerInterval, totalAmount, castle);
    }

    @Override
    public void startGenerator() {
        executor.scheduleAtFixedRate(() -> {

            castle.addResource(new Wood(amountPerInterval));
            totalAmount.addAndGet(-amountPerInterval);

            if (totalAmount.get() == amountPerInterval * 3){
                castle.sendMessage("Лес скоро иссякнет");
                status = Status.ALMOST_REMOVED;

                castle.notifyObservers(new GeneratorEvent(
                        "Forest", Action.ALMOST_REMOVED
                ));

            }

            if (totalAmount.get() <= 0){
                castle.sendMessage("Лес вырублен");
                stopGenerator();
            }

        }, 0, interval, TimeUnit.SECONDS);
    }
}