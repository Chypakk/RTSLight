package chypakk.model;

import chypakk.config.*;
import chypakk.model.building.Building;
import chypakk.model.managers.BuildingManager;
import chypakk.model.managers.GeneratorManager;
import chypakk.model.managers.ResourceManager;
import chypakk.model.managers.UnitManager;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.Resource;
import chypakk.model.units.Unit;
import chypakk.observer.GameObserver;
import chypakk.observer.event.*;

import java.util.*;
import java.util.concurrent.*;

public class Castle implements GameState {
    private int health;
    private final GameConfig config;
    private final ResourceManager resourceManager;
    private final GeneratorManager generatorManager;
    private final BuildingManager buildingManager;
    private final UnitManager unitManager;
    private final List<GameObserver> observers = new CopyOnWriteArrayList<>();

    public Castle(int health, GameConfig config) {
        this.health = health;
        this.config = config;
        this.resourceManager = new ResourceManager(config.resources());
        this.generatorManager = new GeneratorManager();
        this.buildingManager = new BuildingManager();
        this.unitManager = new UnitManager();
    }

    @Override
    public ScheduledFuture<?> scheduleResourceTask(Runnable task, long delay, long period, TimeUnit unit) {
        return generatorManager.scheduleResourceTask(task, delay, period, unit);
    }

    @Override
    public GameConfig getConfig() {
        return config;
    }

    @Override
    public void addResource(Resource res) {
        resourceManager.addResource(res);

        notifyObservers(new ResourceEvent(
                res.getType().name(), Action.ADDED, res.getAmount()
        ));
    }

    @Override
    public void removeResource(ResourceType type, int amount) {
        resourceManager.removeResource(type, amount);

        notifyObservers(new ResourceEvent(
                type.name(), Action.REMOVED, amount
        ));
    }

    @Override
    public int getResource(ResourceType type) {
        return resourceManager.getResource(type);
    }

    @Override
    public void printResources() {
        resourceManager.printResources();
    }

    @Override
    public void addGenerator(ResourceGenerator generator) {
        generatorManager.addGenerator(generator);

        notifyObservers(new GeneratorEvent(
                generator.getClass().getSimpleName(),
                Action.ADDED
        ));
    }

    @Override
    public List<ResourceGenerator> getGenerators(String type) {
        return generatorManager.getGenerators(type);
    }

    @Override
    public int getAlmostRemovedCount(String generatorType) {
        return generatorManager.getAlmostRemovedCount(generatorType);
    }

    @Override
    public void removeGenerator(ResourceGenerator generator) {
        generatorManager.removeGenerator(generator);

        notifyObservers(new GeneratorEvent(
                generator.getClass().getSimpleName(),
                Action.REMOVED
        ));
    }

    @Override
    public void stopAllGenerators() {
        generatorManager.stopAllGenerators();
    }

    @Override
    public void printGenerators() {
        generatorManager.printGenerators();
    }

    @Override
    public void addBuilding(Building building) {
        buildingManager.addBuilding(building);

        notifyObservers(new BuildingEvent(
                building.getName(), Action.ADDED
        ));
    }

    @Override
    public boolean haveBuilding(String name) {
        return buildingManager.haveBuilding(name);
    }

    @Override
    public boolean haveBuilding(Building building) {
        return buildingManager.haveBuilding(building);
    }

    @Override
    public void printBuildings() {
        buildingManager.printBuildings();
    }

    @Override
    public void addUnit(Unit unit) {
        unitManager.addUnit(unit);

        notifyObservers(new UnitEvent(
                unit.getName(),
                Action.ADDED
        ));
    }

    @Override
    public void removeUnit(Unit unit) {
        unitManager.removeUnit(unit);

        notifyObservers(new UnitEvent(
                unit.getName(),
                Action.REMOVED
        ));
    }

    @Override
    public void printUnits() {
        unitManager.printUnits();
    }

    @Override
    public List<Unit> getUnits() {
        return unitManager.getUnits();
    }

    @Override
    public List<Unit> getUnits(String type) {
        return unitManager.getUnits(type);
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void takeDamage(int damage) {
        this.health -= damage;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void sendMessage(String message) {
        synchronized (observers) {
            for (GameObserver observer : observers) {
                observer.onMessage(message);
            }
        }
    }

    @Override
    public void addObserver(GameObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(GameObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.onEvent(event);
        }
    }

    public List<GeneratorConfig> getGeneratorConfigs() {
        return config.generators();
    }

    public List<ResourceConfig> getResourceConfigs() {
        return config.resources();
    }

    public List<BuildingConfig> getBuildingConfigs() {
        return config.buildings();
    }

    public List<UnitConfig> getUnitConfigs() {
        return config.units();
    }
}