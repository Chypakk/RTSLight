package chypakk.model;

import chypakk.model.building.Building;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.Resource;
import chypakk.model.resources.generator.Status;
import chypakk.model.units.Unit;
import chypakk.observer.GameObservable;
import chypakk.observer.GameObserver;
import chypakk.observer.event.*;

import java.util.*;
import java.util.concurrent.*;

public class Castle implements GameObservable {
    private int health;
    private final Map<ResourceType, Resource> resources = new ConcurrentHashMap<>();
    private final List<ResourceGenerator> generators = new CopyOnWriteArrayList<>();
    private final List<Unit> units = new CopyOnWriteArrayList<>();
    private final Set<Building> buildings = ConcurrentHashMap.newKeySet();

    private final List<GameObserver> observers = new CopyOnWriteArrayList<>();

    private final List<GeneratorDisplayConfig> generatorDisplayConfigs = Arrays.asList(
            new GeneratorDisplayConfig("GoldMine", "шахт", 1),
            new GeneratorDisplayConfig("Forest", "лесов", 2)
    );
    private final List<ResourceDisplayConfig> resourceDisplayConfigs = Arrays.asList(
            new ResourceDisplayConfig("GOLD", "золото", 1),
            new ResourceDisplayConfig("WOOD", "дерево", 2)
    );

    private final ScheduledExecutorService resourceExecutor =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r);
                t.setName("Resource-Generator-Thread");
                t.setDaemon(true);
                return t;
            });

    public Castle(int health) {
        this.health = health;
    }

    public ScheduledFuture<?> scheduleResourceTask(Runnable task, long delay, long period, TimeUnit unit) {
        return resourceExecutor.scheduleAtFixedRate(task, delay, period, unit);
    }

    public void addResource(Resource res) {
        synchronized (resources) {
            Resource existing = resources.get(res.getType());
            if (existing != null) {
                existing.addAmount(res.getAmount());
            } else {
                resources.put(res.getType(), res);
            }

            notifyObservers(new ResourceEvent(
                    res.getType().name(), Action.ADDED, res.getAmount()
            ));
        }
    }

    public void removeResource(ResourceType type, int amount) {
        synchronized (resources) {
            resources.get(type).removeAmount(amount);

            notifyObservers(new ResourceEvent(
                    type.name(), Action.REMOVED, amount
            ));
        }
    }

    public int getResource(ResourceType type) {
        synchronized (resources) {
            Resource resource = resources.get(type);
            return resource != null ? resource.getAmount() : 0;
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

    public List<ResourceGenerator> getGenerators(String type) {
        return generators.stream().filter(generator -> generator.getClass().getSimpleName().equals(type)).toList();
    }

    public int getAlmostRemovedCount(String generatorType) {
        return generators.stream().filter(generator ->
                generator.getStatus() == Status.ALMOST_REMOVED && generator.getClass().getSimpleName().equals(generatorType)
        ).toList().size();
    }

    public void addGenerator(ResourceGenerator generator) {
        synchronized (generators) {
            generators.add(generator);
            generator.startGenerator();

            notifyObservers(new GeneratorEvent(
                    generator.getClass().getSimpleName(),
                    Action.ADDED
            ));
        }
    }

    public void removeGenerator(ResourceGenerator generator) {
        synchronized (generators) {
            generators.remove(generator);

            notifyObservers(new GeneratorEvent(
                    generator.getClass().getSimpleName(),
                    Action.REMOVED
            ));
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

    public void addBuilding(Building building) {
        synchronized (buildings) {
            buildings.add(building);

            notifyObservers(new BuildingEvent(
                    building.getName(), Action.ADDED
            ));

        }
    }

    public boolean haveBuilding(String name) {
        Building neededBuilding = buildings.stream().filter(build -> build.getName().equals(name)).findFirst().orElse(null);
        return neededBuilding != null;
    }

    public boolean haveBuilding(Building building) {
        return buildings.contains(building);
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

//    //todo реализовать систему юнитов
//    //todo добавить observer
//    public void addUnit(Unit unit) {
//        synchronized (units) {
//            units.add(unit);
//        }
//    }
//
//    //todo добавить observer
//    public void removeUnit(Unit unit) {
//        synchronized (units) {
//            units.remove(unit);
//        }
//    }
//
//    public void printUnits() {
//        synchronized (units) {
//            if (units.isEmpty()) {
//                System.out.println("Юнитов пока нет");
//                return;
//            }
//            System.out.println("\nЮниты:");
//            for (Unit unit : units) {
//                System.out.println(unit);
//            }
//        }
//    }
//
//    public List<Unit> getUnits() {
//        return units;
//    }
//
//    public int getHealth() {
//        return health;
//    }
//
//    public void takeDamage(int damage) {
//        this.health -= damage;
//    }

    public boolean isAlive() {
        return health >= 0;
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

    public List<ResourceDisplayConfig> getResourceDisplayConfigs(){
        return resourceDisplayConfigs;
    }


}