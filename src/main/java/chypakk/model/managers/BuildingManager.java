package chypakk.model.managers;

import chypakk.model.building.Building;
import chypakk.observer.event.Action;
import chypakk.observer.event.BuildingEvent;
import chypakk.observer.EventNotifier;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BuildingManager implements BuildingManagement {
    private final Set<Building> buildings = ConcurrentHashMap.newKeySet();
    private final EventNotifier eventNotifier;

    public BuildingManager(EventNotifier eventNotifier) {
        this.eventNotifier = eventNotifier;
    }

    @Override
    public void addBuilding(Building building) {
        buildings.add(building);
        eventNotifier.notifyObservers(new BuildingEvent(
                building.getName(), Action.ADDED
        ));
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
