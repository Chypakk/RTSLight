package chypakk.model.game;

import chypakk.config.*;
import chypakk.model.building.Building;
import chypakk.model.managers.BuildingManagement;
import chypakk.model.managers.GeneratorManagement;
import chypakk.model.managers.ResourceManagement;
import chypakk.model.managers.UnitManagement;
import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.model.units.Unit;
import chypakk.observer.GameObservable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface GameState extends GameObservable {
    int getHealth();
    void takeDamage(int damage);
    boolean isAlive();
    boolean isGameActive();
    void setGameActive(boolean active);

    void sendMessage(String message);

    GameConfig getConfig();

    ResourceManagement getResourceManager();
    GeneratorManagement getGeneratorManager();
    BuildingManagement getBuildingManager();
    UnitManagement getUnitManager();
}
