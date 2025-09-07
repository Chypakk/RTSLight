package chypakk.model.resources.generator;

import chypakk.model.Castle;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

//todo добавить стоимость
public abstract class ResourceGenerator {
    protected final int interval;
    protected final int amountPerInterval;
    protected final Castle castle;
    protected final ScheduledExecutorService executor;
    protected Status status;

    protected AtomicInteger totalAmount;

    public ResourceGenerator(int interval, int amountPerInterval, int totalAmount, Castle castle) {
        this.castle = castle;
        this.amountPerInterval = amountPerInterval;
        this.interval = interval;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.totalAmount = new AtomicInteger(totalAmount);
        this.status = Status.RUN;
    }

    public abstract void startGenerator();

    public void stopGenerator(){
        executor.shutdownNow();
        castle.removeGenerator(this);
    }

    public int getAmount() {
        return totalAmount.get();
    }

    public Status getStatus() {
        return status;
    }
}