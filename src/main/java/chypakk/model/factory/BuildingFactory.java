package chypakk.model.factory;

import chypakk.model.building.Barracks;
import chypakk.model.building.Building;
import chypakk.model.building.Marketplace;

public class BuildingFactory {

    public Building createBuilding(String type) {
        return switch (type) {
            case "Marketplace" -> new Marketplace();
            case "Barracks" -> new Barracks();
            default -> throw new IllegalArgumentException("Неизвестный тип здания: " + type);
        };
    }

}
