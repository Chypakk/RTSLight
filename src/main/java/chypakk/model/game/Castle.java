package chypakk.model.game;

import chypakk.config.*;
import chypakk.model.building.Building;
import chypakk.model.managers.*;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.Resource;
import chypakk.model.units.Unit;
import chypakk.observer.GameObserver;
import chypakk.observer.event.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Castle implements GameState {
    private AtomicInteger health;
    private volatile boolean isGameActive;

    private final GameConfig config;
    private final ResourceManagement resourceManager;
    private final GeneratorManagement generatorManager;
    private final BuildingManagement buildingManager;
    private final UnitManagement unitManager;
    private final List<GameObserver> observers = new CopyOnWriteArrayList<>();

    public Castle(int health, GameConfig config) {
        this.health = new AtomicInteger(health);
        this.isGameActive = true;
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
    public boolean trySpendResources(Map<ResourceType, Integer> cost) {
        return resourceManager.tryRemoveResource(cost);
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
        return health.get();
    }

    @Override
    public void takeDamage(int damage) {
        this.health.addAndGet(-damage);
    }

    @Override
    public boolean isAlive() {
        return health.get() > 0;
    }

    @Override
    public boolean isGameActive() {
        return isGameActive && isAlive();
    }

    @Override
    public void setGameActive(boolean active) {
        this.isGameActive = active;
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
}