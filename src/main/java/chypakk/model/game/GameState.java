package chypakk.model.game;

import chypakk.config.*;
import chypakk.model.managers.*;
import chypakk.observer.GameObservable;
import chypakk.observer.MessageNotifier;

//todo сделать тонким и ввести новую абстракцию между ui и логикой
public interface GameState extends GameObservable, MessageNotifier {
    int getHealth();
    void takeDamage(int damage);
    boolean isAlive();
    boolean isGameActive();
    void setGameActive(boolean active);

    void sendMessage(String message);

    GameConfig getConfig();
    GameLog getGameLog();

    ResourceManagement getResourceManager();
    GeneratorManagement getGeneratorManager();
    BuildingManagement getBuildingManager();
    UnitManagement getUnitManager();
}
