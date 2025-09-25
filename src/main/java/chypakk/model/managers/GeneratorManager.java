package chypakk.model.managers;

import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.generator.Status;
import chypakk.observer.event.Action;
import chypakk.observer.EventNotifier;
import chypakk.observer.event.GeneratorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

public class GeneratorManager implements GeneratorManagement {
    private final Logger logger = LoggerFactory.getLogger(GeneratorManager.class);

    private final List<ResourceGenerator> generators = new CopyOnWriteArrayList<>();
    private final EventNotifier eventNotifier;
    private final ScheduledExecutorService resourceExecutor =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setName("Resource-Generator-Thread");
                t.setDaemon(true);
                return t;
            });

    public GeneratorManager(EventNotifier eventNotifier) {
        this.eventNotifier = eventNotifier;
    }

    @Override
    public void addGenerator(ResourceGenerator generator) {
        logger.debug("добавление {}", generator);

        generators.add(generator);
        generator.startGenerator();

        eventNotifier.notifyObservers(new GeneratorEvent(
                generator.getClass().getSimpleName(),
                Action.ADDED
        ));
    }

    @Override
    public List<ResourceGenerator> getGenerators(String type) {
        return generators.stream().filter(generator -> generator.getClass().getSimpleName().equals(type)).toList();
    }

    @Override
    public int getAlmostRemovedCount(String generatorType) {
        return generators.stream().filter(generator ->
                generator.getStatus() == Status.ALMOST_REMOVED && generator.getClass().getSimpleName().equals(generatorType)
        ).toList().size();
    }

    @Override
    public void removeGenerator(ResourceGenerator generator) {
        logger.debug("удаление {}", generator);

        generators.remove(generator);

        eventNotifier.notifyObservers(new GeneratorEvent(
                generator.getClass().getSimpleName(),
                Action.REMOVED
        ));
    }

    @Override
    public void stopAllGenerators() {
        logger.debug("выключение генераторов");
        resourceExecutor.shutdownNow();
        try {
            if (!resourceExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                resourceExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            resourceExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        generators.clear();

        logger.info("Все генераторы остановлены");
    }

    @Override
    public void printGenerators() {
        if (generators.isEmpty()) {
            System.out.println("Генераторов пока нет");
            return;
        }
        System.out.println("\nАктивные генераторы:");
        for (ResourceGenerator gen : generators) {
            System.out.println("- " + gen.getClass().getSimpleName() + ", осталось: " + gen.getAmount());
        }
    }

    @Override
    public ScheduledFuture<?> scheduleResourceTask(Runnable task, long delay, long period, TimeUnit unit) {
        return resourceExecutor.scheduleAtFixedRate(task, delay, period, unit);
    }
}
