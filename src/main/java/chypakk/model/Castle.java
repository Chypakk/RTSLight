package chypakk.model;

import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.resources.Resource;
import chypakk.model.units.Unit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Castle {
    private int health;
    private final Map<String, Resource> resources = new ConcurrentHashMap<>();
    private final List<ResourceGenerator> generators = new CopyOnWriteArrayList<>();
    private final List<Unit> units = new CopyOnWriteArrayList<>();

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
        }
    }

    public void removeResource(String type, int amount) {
        synchronized (resources) {
            resources.get(type).removeAmount(amount);
        }
    }

    public int getResource(String type) {
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
        }
        generator.startGenerator();
        System.out.println("Генератор " + generator.getClass().getSimpleName() + " добавлен!");
    }

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

    //todo реализовать систему юнитов
    public void addUnit(Unit unit) {
        synchronized (units) {
            units.add(unit);
        }
    }

    public void removeUnit(Unit unit) {
        synchronized (units) {
            units.remove(unit);
        }
    }

    public void printUnits() {
        synchronized (units){
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


}