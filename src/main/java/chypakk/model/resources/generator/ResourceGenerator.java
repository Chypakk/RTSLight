package chypakk.model.resources.generator;

import chypakk.model.game.GameState;
import chypakk.model.resources.Resource;
import chypakk.observer.event.Action;
import chypakk.observer.event.GeneratorEvent;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ResourceGenerator {
    protected final int interval;
    protected final int amountPerInterval;
    protected final GameState castle;
    protected Status status;

    protected AtomicInteger totalAmount;

    private ScheduledFuture<?> taskFuture;

    public ResourceGenerator(int interval, int amountPerInterval, int totalAmount, GameState castle) {
        if (interval <= 0 || amountPerInterval <= 0 || totalAmount <= 0) throw new IllegalArgumentException("Некорректные данные генератора");

        this.castle = castle;
        this.amountPerInterval = amountPerInterval;
        this.interval = interval;
        this.totalAmount = new AtomicInteger(totalAmount);
        this.status = Status.RUN;
    }

    public void startGenerator(){
        taskFuture = castle.scheduleResourceTask(this::generateResources, 0, interval, TimeUnit.SECONDS);
    }

    private void generateResources() {
        try {
            if (totalAmount.get() <= 0) {
                stopGenerator();
                return;
            }

            castle.addResource(createResource());
            totalAmount.addAndGet(-amountPerInterval);
            checkStatus();
        } catch (Exception e) {
            handleGenerationError(e);
        }
    }

    protected abstract Resource createResource();

    private void checkStatus() {
        if (totalAmount.get() <= amountPerInterval * 3) {
            status = Status.ALMOST_REMOVED;
            castle.notifyObservers(new GeneratorEvent(
                    getClass().getSimpleName(),
                    Action.ALMOST_REMOVED
            ));
        }
    }

    private void handleGenerationError(Exception e) {
        System.err.println("Ошибка в " + getClass().getSimpleName() + ": " + e.getMessage());
        stopGenerator();
    }

    public void stopGenerator(){
        if (taskFuture != null) {
            taskFuture.cancel(true);
        }
        castle.removeGenerator(this);
    }

    public int getAmount() {
        return totalAmount.get();
    }

    public Status getStatus() {
        return status;
    }
}