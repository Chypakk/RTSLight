package chypakk.composite;

import chypakk.config.ExchangeConfig;
import chypakk.config.GameConfig;
import chypakk.config.UnitConfig;
import chypakk.model.dto.GameCommandDTO;
import chypakk.model.game.GameService;
import chypakk.model.resources.ResourceType;
import chypakk.ui.ConsoleUI;
import chypakk.ui.MenuRender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

//todo полностью переделать
public class MenuSystem {
    private final Logger logger = LoggerFactory.getLogger(MenuSystem.class);

    private final GameService gameService;
    private final MenuRender renderer;

    public MenuSystem(GameService gameService, MenuRender renderer) {
        this.gameService = gameService;
        this.renderer = renderer;
    }

    private MenuComponent buildRootMenu() {
        MenuGroup rootMenu = new MenuGroup("Главное меню", renderer, gameService);
        rootMenu.addItem(1, buildConstructMenu());
        rootMenu.addItem(2, buildUseBuildingMenu());

        if (renderer instanceof ConsoleUI) {
            rootMenu.addItem(3, buildReportsMenu());
        }

        rootMenu.addItem(0, new CommandLeaf(
                        "Выход", () -> gameService.executeCommand(new GameCommandDTO.ExitGame())
                )
        );

        return rootMenu;
    }

    private MenuComponent buildConstructMenu() {
        GameConfig config = gameService.getConfig();
        MenuGroup generatorsMenu = new MenuGroup("Генераторы", renderer, gameService);

        for (int i = 0; i < config.generators().size(); i++) {
            var configGen = config.generators().get(i);
            String description = String.format("Добавить %s (%s)",
                    configGen.label(),
                    formatCost(configGen.cost())
            );

            generatorsMenu.addItem(i + 1, new CommandLeaf(
                            description,
                            () -> gameService.executeCommand(new GameCommandDTO.AddGenerator(configGen.type()))
                    )
            );
        }

        MenuGroup construction = new MenuGroup("Здания", renderer, gameService);
        for (int i = 0; i < config.buildings().size(); i++) {
            var configBuild = config.buildings().get(i);
            String description = String.format("Добавить %s (%s)",
                    configBuild.label(),
                    formatCost(configBuild.cost())
            );

            construction.addItem(i + 1, new CommandLeaf(
                    description,
                    () -> gameService.executeCommand(new GameCommandDTO.AddBuilding(configBuild.type())),
                    () -> !gameService.hasBuilding(configBuild.label())
            ));
        }

        MenuGroup buildMenu = new MenuGroup("Построить", renderer, gameService);
        buildMenu.addItem(1, generatorsMenu);
        buildMenu.addItem(2, construction);

        return buildMenu;
    }

    private MenuComponent buildUseBuildingMenu() {
        GameConfig config = gameService.getConfig();

        MenuGroup buildingsUseMenu = new MenuGroup("Здания", renderer, gameService);
        MenuGroup marketMenu = new MenuGroup("Рынок", renderer, gameService);
        for (int i = 0; i < config.exchanges().size(); i++) {
            ExchangeConfig exchange = config.exchanges().get(i);
            marketMenu.addItem(i + 1, new CommandLeaf(
                    String.format("Обменять %d %s на %d %s",
                            exchange.fromAmount(),
                            getResourceLabel(exchange.fromType()),
                            exchange.toAmount(),
                            getResourceLabel(exchange.toType())),

                    () -> gameService.executeCommand(new GameCommandDTO.ExchangeResource(
                            exchange.fromType(),
                            exchange.fromAmount(),
                            exchange.toType(),
                            exchange.toAmount()
                    )),
                    () -> gameService.hasBuilding("Рынок")
            ));
        }

        MenuGroup barracksMenu = new MenuGroup("Казармы", renderer, gameService);
        for (int i = 0; i < config.units().size(); i++) {
            UnitConfig unitConfig = config.units().get(i);
            barracksMenu.addItem(i + 1, new CommandLeaf(
                    String.format("Нанять %s (%s)",
                            unitConfig.label(),
                            formatCost(unitConfig.cost())),

                    () -> gameService.executeCommand(new GameCommandDTO.RecruitUnit(unitConfig.type())),
                    () -> gameService.hasBuilding("Казармы")
            ));
        }

        buildingsUseMenu.addItem(1, marketMenu);
        buildingsUseMenu.addItem(2, barracksMenu);

        return buildingsUseMenu;
    }

    private MenuComponent buildReportsMenu() {
        MenuGroup reportsMenu = new MenuGroup("Отчеты", renderer, gameService);
        reportsMenu.addItem(1, new CommandLeaf(
                        "Ресурсы",
                        () -> gameService.executeCommand(new GameCommandDTO.ShowResources())
                )
        );
        reportsMenu.addItem(2, new CommandLeaf(
                        "Генераторы",
                        () -> gameService.executeCommand(new GameCommandDTO.ShowGenerators())
                )
        );
        reportsMenu.addItem(3, new CommandLeaf(
                        "Здания",
                        () -> gameService.executeCommand(new GameCommandDTO.ShowBuildings())
                )
        );

        return reportsMenu;
    }

    public void start() {
        MenuComponent menu = buildRootMenu();
        menu.execute();
    }

    private String formatCost(Map<String, Integer> cost) {
        return cost.entrySet().stream()
                .map(e -> e.getValue() + " " + getResourceLabel(e.getKey()))
                .collect(Collectors.joining(", "));
    }

    private Map<ResourceType, Integer> convertCost(Map<String, Integer> stringCost) {
        return stringCost.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> {
                            ResourceType type = ResourceType.fromType(e.getKey());
                            if (type == null) {
                                logger.error("Неизвестный тип ресурса: {}", e.getKey());
                                return null;
                            }
                            return type;
                        },
                        Map.Entry::getValue
                ));
    }

    private String getResourceLabel(String resourceType) {
        return gameService.getConfig().resources().stream()
                .filter(r -> r.type().equals(resourceType))
                .findFirst()
                .map(chypakk.config.ResourceConfig::label)
                .orElse(resourceType.toLowerCase());
    }
}
