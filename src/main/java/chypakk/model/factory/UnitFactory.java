package chypakk.model.factory;

import chypakk.config.GameConfig;
import chypakk.config.UnitConfig;
import chypakk.model.units.Archer;
import chypakk.model.units.Soldier;
import chypakk.model.units.Unit;

public class UnitFactory {
    private final GameConfig config;

    public UnitFactory(GameConfig config) {
        this.config = config;
    }

    public Unit createUnit(String type) {
        UnitConfig unitConfig = getUnitConfig(type);

        return switch (type) {
            case "Soldier" -> new Soldier(unitConfig.label(), unitConfig.health(), unitConfig.baseDamage());
            case "Archer" -> new Archer(unitConfig.label(), unitConfig.health(), unitConfig.baseDamage());
            default -> throw new IllegalArgumentException("Неизвестный тип юнита: " + type);
        };
    }

    public UnitConfig getUnitConfig(String type) {
        return config.units().stream()
                .filter(u -> u.type().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный тип юнита: " + type));
    }
}
