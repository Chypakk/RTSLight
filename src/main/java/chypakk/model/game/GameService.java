package chypakk.model.game;

import chypakk.config.GameConfig;
import chypakk.model.dto.GameCommandDTO;
import chypakk.model.dto.GameObserverDTO;

import java.util.List;
import java.util.function.Consumer;

public interface GameService {
    void executeCommand(GameCommandDTO command);
    void subscribeToEvents(Consumer<GameObserverDTO> handler);
    boolean isGameActive();
    GameConfig getConfig();
    boolean hasBuilding(String name);
    int getResourceAmount(String resourceType);
    int getGeneratorCount(String generatorType);
    int getGeneratorAlmostRemovedCount(String generatorType);
    int getUnitCount(String unitLabel);
    List<String> getGameLogMessages();
}
