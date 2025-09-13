package chypakk.model;

import chypakk.config.BuildingDisplayConfig;
import chypakk.config.GeneratorDisplayConfig;
import chypakk.config.ResourceDisplayConfig;
import chypakk.config.UnitDisplayConfig;
import chypakk.model.building.Building;
import chypakk.model.managers.BuildingManager;
import chypakk.model.managers.GeneratorManager;
import chypakk.model.managers.ResourceManager;
import chypakk.model.managers.UnitManager;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.Resource;
import chypakk.model.units.Unit;
import chypakk.observer.GameObservable;
import chypakk.observer.GameObserver;
import chypakk.observer.event.*;

import java.util.*;
import java.util.concurrent.*;

public class Castle implements GameObservable {
    private int health;

    private final ResourceManager resourceManager;
    private final GeneratorManager generatorManager;
    private final BuildingManager buildingManager;
    private final UnitManager unitManager;
    private final List<GameObserver> observers = new CopyOnWriteArrayList<>();

    private final List<GeneratorDisplayConfig> generatorDisplayConfigs = Arrays.asList(
            new GeneratorDisplayConfig("GoldMine", "шахт", 1),
            new GeneratorDisplayConfig("Forest", "лесов", 2)
    );
    private final List<ResourceDisplayConfig> resourceDisplayConfigs = Arrays.asList(
            new ResourceDisplayConfig("GOLD", "золото", 1),
            new ResourceDisplayConfig("WOOD", "дерево", 2)
    );
    private final List<BuildingDisplayConfig> buildingDisplayConfigs = Arrays.asList(
            new BuildingDisplayConfig("Marketplace", "Рынок"),
            new BuildingDisplayConfig("Barracks", "Казармы")
    );

    private final List<UnitDisplayConfig> unitDisplayConfigs = Arrays.asList(
            new UnitDisplayConfig("Soldier", "Рыцарь"),
            new UnitDisplayConfig("Archer", "Лучник")
    );

    public Castle(int health) {
        this.health = health;
        this.resourceManager = new ResourceManager();
        this.generatorManager = new GeneratorManager();
        this.buildingManager = new BuildingManager();
        this.unitManager = new UnitManager();
    }

    public ScheduledFuture<?> scheduleResourceTask(Runnable task, long delay, long period, TimeUnit unit) {
        return generatorManager.scheduleResourceTask(task, delay, period, unit);
    }

    public void addResource(Resource res) {
        resourceManager.addResource(res);

        notifyObservers(new ResourceEvent(
                res.getType().name(), Action.ADDED, res.getAmount()
        ));
    }

    public void removeResource(ResourceType type, int amount) {
        resourceManager.removeResource(type, amount);

        notifyObservers(new ResourceEvent(
                type.name(), Action.REMOVED, amount
        ));
    }

    public int getResource(ResourceType type) {
        return resourceManager.getResource(type);
    }

    public void printResources() {
        resourceManager.printResources();
    }

    public void addGenerator(ResourceGenerator generator) {
        generatorManager.addGenerator(generator);

        notifyObservers(new GeneratorEvent(
                generator.getClass().getSimpleName(),
                Action.ADDED
        ));
    }

    public List<ResourceGenerator> getGenerators(String type) {
        return generatorManager.getGenerators(type);
    }

    public int getAlmostRemovedCount(String generatorType) {
        return generatorManager.getAlmostRemovedCount(generatorType);
    }

    public void removeGenerator(ResourceGenerator generator) {
        generatorManager.removeGenerator(generator);

        notifyObservers(new GeneratorEvent(
                generator.getClass().getSimpleName(),
                Action.REMOVED
        ));
    }

    public void stopAllGenerators() {
        generatorManager.stopAllGenerators();
    }

    public void printGenerators() {
        generatorManager.printGenerators();
    }

    public void addBuilding(Building building) {
        buildingManager.addBuilding(building);

        notifyObservers(new BuildingEvent(
                building.getName(), Action.ADDED
        ));
    }

    public boolean haveBuilding(String name) {
        return buildingManager.haveBuilding(name);
    }

    public boolean haveBuilding(Building building) {
        return buildingManager.haveBuilding(building);
    }

    public void printBuildings() {
        buildingManager.printBuildings();
    }

    public void addUnit(Unit unit) {
        unitManager.addUnit(unit);

        notifyObservers(new UnitEvent(
                unit.getName(),
                Action.ADDED
        ));
    }

    public void removeUnit(Unit unit) {
        unitManager.removeUnit(unit);

        notifyObservers(new UnitEvent(
                unit.getName(),
                Action.REMOVED
        ));
    }

    public void printUnits() {
        unitManager.printUnits();
    }

    public List<Unit> getUnits() {
        return unitManager.getUnits();
    }

    public List<Unit> getUnits(String type) {
        return unitManager.getUnits(type);
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
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

    public void sendMessage(String message) {
        synchronized (observers) {
            for (GameObserver observer : observers) {
                observer.onMessage(message);
            }
        }
    }

    public List<GeneratorDisplayConfig> getGeneratorDisplayConfigs() {
        return generatorDisplayConfigs;
    }

    public List<ResourceDisplayConfig> getResourceDisplayConfigs() {
        return resourceDisplayConfigs;
    }

    public List<BuildingDisplayConfig> getBuildingDisplayConfigs() {
        return buildingDisplayConfigs;
    }

    public List<UnitDisplayConfig> getUnitDisplayConfigs() {
        return unitDisplayConfigs;
    }
}