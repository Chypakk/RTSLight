package chypakk.model.managers;

import chypakk.model.building.Building;

import java.util.Set;

public interface BuildingManagement {
    void addBuilding(Building building);
    boolean haveBuilding(String name);
    boolean haveBuilding(Building building);
    Set<Building> getBuildings();
    void printBuildings();
}
