package chypakk.model.resources.generator;

import chypakk.model.Castle;
import chypakk.model.resources.Wood;

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
            //System.out.printf("[Лес] Добавлено %d дерева (осталось: %d)\n", amountPerInterval, totalAmount.get());

            if (totalAmount.get() == amountPerInterval * 3){
                castle.sendMessage("Лес скоро иссякнет");
                //System.out.println("Лес скоро иссякнет");
            }

            if (totalAmount.get() <= 0){
                castle.sendMessage("Лес вырублен");
                //System.out.println();
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