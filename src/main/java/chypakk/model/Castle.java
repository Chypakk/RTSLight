package chypakk.model;

import chypakk.model.building.Building;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.Resource;
import chypakk.model.units.Unit;
import chypakk.observer.GameObservable;
import chypakk.observer.GameObserver;
import chypakk.observer.event.GameEvent;
import chypakk.observer.event.GeneratorEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Castle implements GameObservable {
    private int health;
    private final Map<ResourceType, Resource> resources = new ConcurrentHashMap<>();
    private final List<ResourceGenerator> generators = new CopyOnWriteArrayList<>();
    private final List<Unit> units = new CopyOnWriteArrayList<>();
    private final Set<Building> buildings = ConcurrentHashMap.newKeySet();

    private final List<GameObserver> observers = new CopyOnWriteArrayList<>();

    public Castle(int health) {
        this.health = health;
    }

    public void addResource(Resource res) {
        synchronized (resources) {
            Resource existing = resources.get(res.getType());
            if (existing != null) {
                existing.addAmount(res.getAmount());
            } else {
                resources.put(res.getType(), res);
            }

            notifyObservers();
            sendMessage("Добавлен ресурс: " + res.getType());
        }
    }

    //todo добавить observer
    public void removeResource(ResourceType type, int amount) {
        synchronized (resources) {
            resources.get(type).removeAmount(amount);
        }
    }

    public int getResource(ResourceType type) {
        synchronized (resources) {
            return resources.get(type).getAmount();
        }
    }

    public void printResources() {
        synchronized (resources) {
            if (resources.isEmpty()) {
                System.out.println("Ресурсов пока нет");
                return;
            }
            for (Resource res : resources.values()) {
                System.out.println(res);
            }
        }
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

    public List<ResourceGenerator> getGenerators() {
        return generators;
    }

    public void addGenerator(ResourceGenerator generator) {
        synchronized (generators) {
            generators.add(generator);
            generator.startGenerator();

            //notifyObservers();
            notifyObservers(new GeneratorEvent(
                    generator.getClass().getSimpleName(),
                    GeneratorEvent.Action.ADDED
            ));
            //sendMessage("Добавлен генератор: " + generator.getClass().getSimpleName());
        }
    }

    //todo добавить observer
    public void removeGenerator(ResourceGenerator generator) {
        synchronized (generators) {
            generators.remove(generator);
        }
    }

    public void stopAllGenerators() {
        synchronized (generators) {
            for (ResourceGenerator generator : generators) {
                generator.stopGenerator();
            }
        }
        System.out.println("Все генераторы остановлены");
    }

    public void addBuilding(Building building) {
        synchronized (buildings) {
            buildings.add(building);

            notifyObservers();
            sendMessage("Построено здание: " + building.getName());
        }
    }

    public boolean haveBuilding(String name) {
        Building neededBuilding = buildings.stream().filter(build -> build.getName().equals(name)).findFirst().orElse(null);
        return neededBuilding != null;
    }

    public void printBuildings() {
        synchronized (buildings) {
            if (buildings.isEmpty()) {
                System.out.println("Зданий пока нет");
                return;
            }
            System.out.println("\nЗдания:");
            for (Building gen : buildings) {
                System.out.println("- " + gen.getClass().getSimpleName());
            }
        }
    }

    //todo реализовать систему юнитов
    //todo добавить observer
    public void addUnit(Unit unit) {
        synchronized (units) {
            units.add(unit);
        }
    }

    //todo добавить observer
    public void removeUnit(Unit unit) {
        synchronized (units) {
            units.remove(unit);
        }
    }

    public void printUnits() {
        synchronized (units) {
            if (units.isEmpty()) {
                System.out.println("Юнитов пока нет");
                return;
            }
            System.out.println("\nЮниты:");
            for (Unit unit : units) {
                System.out.println(unit);
            }
        }
    }

    public List<Unit> getUnits() {
        return units;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public boolean isAlive() {
        return health >= 0;
    }

    @Override
    public void addObserver(GameObserver observer) {
        synchronized (observers) {
            observers.add(observer);
            notifyObservers();
        }
    }

    @Override
    public void removeObserver(GameObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers() {
        GameState state = new GameState(
                resources,
                generators.stream().map(g -> g.getClass().getSimpleName()).toList(),
                buildings.stream().map(Building::getName).toList()
        );

        synchronized (observers) {
            for (GameObserver observer : observers) {
                observer.onGameStateChanged(state);
            }
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
}