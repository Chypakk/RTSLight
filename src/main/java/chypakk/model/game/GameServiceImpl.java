package chypakk.model.game;

import chypakk.composite.command.*;
import chypakk.config.BuildingConfig;
import chypakk.config.GameConfig;
import chypakk.config.GeneratorConfig;
import chypakk.config.UnitConfig;
import chypakk.model.dto.GameCommandDTO;
import chypakk.model.dto.GameObserverDTO;
import chypakk.model.factory.BuildingFactory;
import chypakk.model.factory.GeneratorFactory;
import chypakk.model.factory.UnitFactory;
import chypakk.model.resources.ResourceType;
import chypakk.observer.GameObserver;
import chypakk.observer.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameServiceImpl implements GameService{
    private final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final List<Consumer<GameObserverDTO>> observers = new ArrayList<>();
    private final GameState castle;
    private final GeneratorFactory generatorFactory;
    private final BuildingFactory buildingFactory;
    private final UnitFactory unitFactory;

    public GameServiceImpl(GameState castle) {
        this.castle = castle;
        this.generatorFactory = new GeneratorFactory(castle.getConfig());
        this.buildingFactory = new BuildingFactory();
        this.unitFactory = new UnitFactory(castle.getConfig());

        castle.addObserver(new GameObserver() {
            @Override
            public void onMessage(String message) {
                notifyObservers(new GameObserverDTO.Message(message));
            }

            @Override
            public void onEvent(GameEvent event) {
                GameObserverDTO dto = convertEvent(event);
                if (dto != null) notifyObservers(dto);
            }
        });
    }

    private GameObserverDTO convertEvent(GameEvent event) {
        return switch (event) {
            case ResourceEvent res -> new GameObserverDTO.ResourceChanged(
                    ResourceType.fromType(res.getType()), res.getAmount()
            );
            case BuildingEvent b -> new GameObserverDTO.BuildingAdded(b.getType());
            case GeneratorEvent g when g.getAction() == Action.ADDED ->
                    new GameObserverDTO.GeneratorAdded(g.getType());
            case GeneratorEvent g when g.getAction() == Action.ALMOST_REMOVED ->
                    new GameObserverDTO.GeneratorAlmostRemoved(g.getType());
            case UnitEvent u when u.getAction() == Action.ADDED -> new GameObserverDTO.UnitAdded(u.getType());
            default -> null;
        };
    }

    private void notifyObservers(GameObserverDTO event) {
        observers.forEach(handler -> handler.accept(event));
    }

    @Override
    public void executeCommand(GameCommandDTO command) {
        GameCommand internalCommand = convertToInternalCommand(command);
        if (internalCommand != null) {
            internalCommand.execute(castle);
        }
    }

    private GameCommand convertToInternalCommand(GameCommandDTO dto) {
        return switch (dto) {
            case GameCommandDTO.AddBuilding(String type) -> {
                var config = findBuildingConfig(type);
                if (config == null) yield null;
                yield new AddBuildingCommand(
                        buildingFactory.createBuilding(type),
                        convertCost(config.cost()),
                        castle.getBuildingManager(),
                        castle.getResourceManager()
                );
            }
            case GameCommandDTO.AddGenerator(String type) -> {
                var config = findGeneratorConfig(type);
                if (config == null) yield null;
                yield new AddGeneratorCommand(
                        () -> generatorFactory.createGenerator(type, castle),
                        convertCost(config.cost()),
                        castle.getGeneratorManager(),
                        castle.getResourceManager()
                );
            }
            case GameCommandDTO.RecruitUnit(String type) -> {
                var config = findUnitConfig(type);
                if (config == null) yield null;
                yield new RecruitCommand(
                        unitFactory.createUnit(type),
                        convertCost(config.cost()),
                        castle.getUnitManager(),
                        castle.getResourceManager()
                );
            }
            case GameCommandDTO.ExchangeResource(String from, int fromAmt, String to, int toAmt) ->
                    new ExchangeResourceCommand(
                            ResourceType.fromType(from),
                            fromAmt,
                            ResourceType.fromType(to),
                            toAmt,
                            castle.getBuildingManager(),
                            castle.getResourceManager()
                    );
            case GameCommandDTO.ShowResources() ->
                    new ShowResourcesCommand(castle.getResourceManager());
            case GameCommandDTO.ShowBuildings() ->
                    new ShowBuildingsCommand(castle.getBuildingManager());
            case GameCommandDTO.ShowGenerators() ->
                    new ShowGeneratorsCommand(castle.getGeneratorManager());
            case GameCommandDTO.ExitGame() ->
                    new ExitCommand(castle.getGeneratorManager());

            default -> {
                logger.error("Unexpected value: {}", dto);
                yield null;
            }
        };
    }

    private BuildingConfig findBuildingConfig(String type) {
        return castle.getConfig().buildings().stream()
                .filter(c -> c.type().equals(type))
                .findFirst()
                .orElse(null);
    }

    private GeneratorConfig findGeneratorConfig(String type) {
        return castle.getConfig().generators().stream()
                .filter(c -> c.type().equals(type))
                .findFirst()
                .orElse(null);
    }

    private UnitConfig findUnitConfig(String type) {
        return castle.getConfig().units().stream()
                .filter(c -> c.type().equals(type))
                .findFirst()
                .orElse(null);
    }

    private Map<ResourceType, Integer> convertCost(Map<String, Integer> stringCost) {
        return stringCost.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> ResourceType.fromType(e.getKey()),
                        Map.Entry::getValue
                ));
    }

    @Override
    public void subscribeToEvents(Consumer<GameObserverDTO> handler) {
        observers.add(handler);
    }

    @Override
    public boolean isGameActive() {
        return castle.isGameActive();
    }

    @Override
    public GameConfig getConfig() {
        return castle.getConfig();
    }

    @Override
    public boolean hasBuilding(String name) {
        return castle.getBuildingManager().haveBuilding(name);
    }

    @Override
    public int getResourceAmount(String resourceType) {
        ResourceType type = ResourceType.fromType(resourceType);
        return type != null ? castle.getResourceManager().getResource(type) : 0;
    }

    @Override
    public int getGeneratorCount(String generatorType) {
        return castle.getGeneratorManager().getGenerators(generatorType).size();
    }

    @Override
    public int getGeneratorAlmostRemovedCount(String generatorType) {
        return castle.getGeneratorManager().getAlmostRemovedCount(generatorType);
    }

    @Override
    public int getUnitCount(String unitLabel) {
        return castle.getUnitManager().getUnits(unitLabel).size();
    }

    @Override
    public List<String> getGameLogMessages() {
        return new ArrayList<>(castle.getGameLog().getMessages());
    }

    public GameState getGameState(){
        return castle;
    }
}
