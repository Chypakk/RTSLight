package chypakk.model.resources.generator;

import chypakk.model.Castle;
import chypakk.model.resources.Gold;

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

            //System.out.printf("[Шахта] Добавлено %d золота (осталось: %d)\n",amountPerInterval, totalAmount.get());

            if (totalAmount.get() == amountPerInterval * 3){
                System.out.println("Золотая шахта скоро иссякнет");
            }

            if (totalAmount.get() <= 0){
                System.out.println("Золотая шахта иссякла");
                stopGenerator();
            }

        }, 0, interval, TimeUnit.SECONDS);
    }

    @Override
    public void stopGenerator() {
        executor.shutdownNow();
        castle.removeGenerator(this);
    }
}