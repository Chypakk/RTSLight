package chypakk.model;

import chypakk.config.*;
import chypakk.model.building.Building;
import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.units.Unit;
import chypakk.observer.GameObservable;
import chypakk.observer.GameObserver;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface GameState extends GameObservable {
    ScheduledFuture<?> scheduleResourceTask(Runnable task, long delay, long period, TimeUnit unit);


    void addResource(Resource res);
    void removeResource(ResourceType type, int amount);
    int getResource(ResourceType type);
    void printResources();

    void addGenerator(ResourceGenerator generator);
    List<ResourceGenerator> getGenerators(String type);
    int getAlmostRemovedCount(String generatorType);
    void removeGenerator(ResourceGenerator generator);
    void stopAllGenerators();
    void printGenerators();

    void addBuilding(Building building);
    boolean haveBuilding(String name);
    boolean haveBuilding(Building building);
    void printBuildings();

    void addUnit(Unit unit);
    void removeUnit(Unit unit);
    void printUnits();
    List<Unit> getUnits();
    List<Unit> getUnits(String type);

    int getHealth();
    void takeDamage(int damage);
    boolean isAlive();

    void sendMessage(String message);
    GameConfig getConfig();

    List<GeneratorConfig> getGeneratorConfigs();
    List<ResourceConfig> getResourceConfigs();
    List<BuildingConfig> getBuildingConfigs();
    List<UnitConfig> getUnitConfigs();

}
