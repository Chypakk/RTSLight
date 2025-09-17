package chypakk.model.factory;

import chypakk.model.building.Barracks;
import chypakk.model.building.Building;
import chypakk.model.building.Marketplace;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BuildingFactory {
    private final Map<String, Supplier<Building>> buildingCreators;

    public BuildingFactory() {
        this.buildingCreators = createBuildingMap();
    }

    private Map<String, Supplier<Building>> createBuildingMap() {
        Map<String, Supplier<Building>> map = new HashMap<>();
        map.put("Marketplace", Marketplace::new);
        map.put("Barracks", Barracks::new);

        return map;
    }

    public Building createBuilding(String type) {
        Supplier<Building> creator = buildingCreators.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("Неизвестный тип здания: " + type);
        }
        return creator.get();
    }

}
