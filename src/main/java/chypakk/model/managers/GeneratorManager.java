package chypakk.model.managers;

import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.generator.Status;

import java.util.List;
import java.util.concurrent.*;

public class GeneratorManager {
    private final List<ResourceGenerator> generators = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService resourceExecutor =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setName("Resource-Generator-Thread");
                t.setDaemon(true);
                return t;
            });

    public void addGenerator(ResourceGenerator generator) {
        synchronized (generators) {
            generators.add(generator);
            generator.startGenerator();
        }
    }

    public List<ResourceGenerator> getGenerators(String type) {
        return generators.stream().filter(generator -> generator.getClass().getSimpleName().equals(type)).toList();
    }

    public int getAlmostRemovedCount(String generatorType) {
        return generators.stream().filter(generator ->
                generator.getStatus() == Status.ALMOST_REMOVED && generator.getClass().getSimpleName().equals(generatorType)
        ).toList().size();
    }

    public void removeGenerator(ResourceGenerator generator) {
        synchronized (generators) {
            generators.remove(generator);
        }
    }

    public void stopAllGenerators() {
        resourceExecutor.shutdownNow();
        try {
            if (!resourceExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                resourceExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            resourceExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        synchronized (generators) {
            generators.clear();
        }
        System.out.println("Все генераторы остановлены");
    }

    public void printGenerators() {
        synchronized (generators) {
            if (generators.isEmpty()) {
                System.out.println("Генераторов пока нет");
                return;
            }
            System.out.println("\nАктивные генераторы:");
            for (ResourceGenerator gen : generators) {
                System.out.println("- " + gen.getClass().getSimpleName() + ", осталось: " + gen.getAmount());
            }
        }
    }

    public ScheduledFuture<?> scheduleResourceTask(Runnable task, long delay, long period, TimeUnit unit) {
        return resourceExecutor.scheduleAtFixedRate(task, delay, period, unit);
    }
}
