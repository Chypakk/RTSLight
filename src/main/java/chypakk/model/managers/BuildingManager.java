package chypakk.model.managers;

import chypakk.model.building.Building;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BuildingManager {
    private final Set<Building> buildings = ConcurrentHashMap.newKeySet();

    public void addBuilding(Building building) {
        synchronized (buildings) {
            buildings.add(building);
        }
    }

    public boolean haveBuilding(String name) {
        Building neededBuilding = buildings.stream().filter(build -> build.getName().equals(name)).findFirst().orElse(null);
        return neededBuilding != null;
    }

    public boolean haveBuilding(Building building) {
        return buildings.contains(building);
    }

    public Set<Building> getBuildings() {
        return buildings;
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
}
