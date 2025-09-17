package chypakk.model.managers;

import chypakk.model.resources.generator.ResourceGenerator;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface GeneratorManagement {
    void addGenerator(ResourceGenerator generator);
    List<ResourceGenerator> getGenerators(String type);
    int getAlmostRemovedCount(String generatorType);
    void removeGenerator(ResourceGenerator generator);
    void stopAllGenerators();
    void printGenerators();
    ScheduledFuture<?> scheduleResourceTask(Runnable task, long delay, long period, TimeUnit unit);
}
