package chypakk.model.managers;

import chypakk.model.building.Building;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BuildingManager implements BuildingManagement {
    private final Set<Building> buildings = ConcurrentHashMap.newKeySet();

    @Override
    public void addBuilding(Building building) {
            buildings.add(building);
    }

    @Override
    public boolean haveBuilding(String name) {
        Building neededBuilding = buildings.stream().filter(build -> build.getName().equals(name)).findFirst().orElse(null);
        return neededBuilding != null;
    }

    @Override
    public boolean haveBuilding(Building building) {
        return buildings.contains(building);
    }

    @Override
    public Set<Building> getBuildings() {
        return buildings;
    }

    @Override
    public void printBuildings() {
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
