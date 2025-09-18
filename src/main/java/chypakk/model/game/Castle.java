package chypakk.model.game;

import chypakk.config.*;
import chypakk.model.managers.*;
import chypakk.observer.GameObserver;
import chypakk.observer.event.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Castle implements GameState, EventNotifier {
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
        this.resourceManager = new ResourceManager(config.resources(), this);
        this.generatorManager = new GeneratorManager(this);
        this.buildingManager = new BuildingManager(this);
        this.unitManager = new UnitManager(this);
    }
    
    @Override
    public GameConfig getConfig() {
        return config;
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
        for (GameObserver observer : observers) {
            observer.onMessage(message);
        }
    }

    @Override
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.onEvent(event);
        }
    }


    public ResourceManagement getResourceManager() {
        return resourceManager;
    }

    public GeneratorManagement getGeneratorManager() {
        return generatorManager;
    }

    public BuildingManagement getBuildingManager() {
        return buildingManager;
    }

    public UnitManagement getUnitManager() {
        return unitManager;
    }
}