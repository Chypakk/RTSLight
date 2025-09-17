package chypakk.model.managers;

import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.generator.Status;

import java.util.List;
import java.util.concurrent.*;

public class GeneratorManager implements GeneratorManagement {
    private final List<ResourceGenerator> generators = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService resourceExecutor =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setName("Resource-Generator-Thread");
                t.setDaemon(true);
                return t;
            });

    @Override
    public void addGenerator(ResourceGenerator generator) {
        synchronized (generators) {
            generators.add(generator);
            generator.startGenerator();
        }
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
        synchronized (generators) {
            generators.remove(generator);
        }
    }

    @Override
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

    @Override
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

    @Override
    public ScheduledFuture<?> scheduleResourceTask(Runnable task, long delay, long period, TimeUnit unit) {
        return resourceExecutor.scheduleAtFixedRate(task, delay, period, unit);
    }
}
