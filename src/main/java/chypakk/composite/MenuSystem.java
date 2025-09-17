package chypakk.composite;

import chypakk.composite.command.*;
import chypakk.config.ExchangeConfig;
import chypakk.config.UnitConfig;
import chypakk.model.game.GameState;
import chypakk.model.factory.BuildingFactory;
import chypakk.model.factory.GeneratorFactory;
import chypakk.model.factory.UnitFactory;
import chypakk.model.resources.ResourceType;
import chypakk.ui.MenuRender;

import java.util.Map;
import java.util.stream.Collectors;

public class MenuSystem {
    private final GameState castle;
    private final MenuRender renderer;
    private final GeneratorFactory generatorFactory;
    private final BuildingFactory buildingFactory;
    private final UnitFactory unitFactory;

    public MenuSystem(GameState castle, MenuRender renderer) {
        this.castle = castle;
        this.renderer = renderer;
        this.generatorFactory = new GeneratorFactory(castle.getConfig());
        this.buildingFactory = new BuildingFactory();
        this.unitFactory = new UnitFactory(castle.getConfig());
    }

    private MenuComponent buildRootMenu() {
        MenuGroup rootMenu = new MenuGroup("Главное меню", renderer);
        rootMenu.addItem(1, buildConstructMenu());
        rootMenu.addItem(2, buildUseBuildingMenu());

        if (renderer.getClass().getSimpleName().toLowerCase().contains("console")) {
            rootMenu.addItem(3, buildReportsMenu());
        }

        rootMenu.addItem(0, new CommandLeaf("Выход", new ExitCommand()));

        return rootMenu;
    }

    private MenuComponent buildConstructMenu() {
        MenuGroup generatorsMenu = new MenuGroup("Генераторы", renderer);
        for (int i = 0; i < castle.getConfig().generators().size(); i++) {
            var config = castle.getConfig().generators().get(i);
            String description = String.format("Добавить %s (%s)",
                    config.label(),
                    formatCost(config.cost())
            );

            generatorsMenu.addItem(i + 1, new CommandLeaf(
                    description,
                    new AddGeneratorCommand(
                            () -> generatorFactory.createGenerator(config.type(), castle),
                            convertCost(config.cost())
                    )
            ));
        }

        MenuGroup construction = new MenuGroup("Здания", renderer);
        for (int i = 0; i < castle.getConfig().buildings().size(); i++) {
            var config = castle.getConfig().buildings().get(i);
            String description = String.format("Добавить %s (%s)",
                    config.label(),
                    formatCost(config.cost())
            );

            construction.addItem(i + 1, new CommandLeaf(
                    description,
                    new AddBuildingCommand(
                            buildingFactory.createBuilding(config.type()),
                            convertCost(config.cost())
                    ),
                    castle -> !castle.haveBuilding(config.label())
            ));
        }

        MenuGroup buildMenu = new MenuGroup("Построить", renderer);
        buildMenu.addItem(1, generatorsMenu);
        buildMenu.addItem(2, construction);

        return buildMenu;
    }

    private MenuComponent buildUseBuildingMenu() {
        MenuGroup buildingsUseMenu = new MenuGroup("Здания", renderer);

        MenuGroup marketMenu = new MenuGroup("Рынок", renderer);
        for (int i = 0; i < castle.getConfig().exchanges().size(); i++) {
            ExchangeConfig exchange = castle.getConfig().exchanges().get(i);
            marketMenu.addItem(i + 1, new CommandLeaf(
                    String.format("Обменять %d %s на %d %s",
                            exchange.fromAmount(),
                            getResourceLabel(exchange.fromType()),
                            exchange.toAmount(),
                            getResourceLabel(exchange.toType())),
                    new ExchangeResourceCommand(
                            ResourceType.valueOf(exchange.fromType()),
                            exchange.fromAmount(),
                            ResourceType.valueOf(exchange.toType()),
                            exchange.toAmount()
                    ),
                    castle -> castle.haveBuilding("Рынок")
            ));
        }

        MenuGroup barracksMenu = new MenuGroup("Казармы", renderer);
        for (int i = 0; i < castle.getConfig().units().size(); i++) {
            UnitConfig config = castle.getConfig().units().get(i);
            barracksMenu.addItem(i + 1, new CommandLeaf(
                    String.format("Нанять %s (%s)",
                            config.label(),
                            formatCost(config.cost())),
                    new RecruitCommand(
                            unitFactory.createUnit(config.type()),
                            convertCost(config.cost())
                    ),
                    castle -> castle.haveBuilding("Казармы")
            ));
        }

        buildingsUseMenu.addItem(1, marketMenu);
        buildingsUseMenu.addItem(2, barracksMenu);

        return buildingsUseMenu;
    }

    private MenuComponent buildReportsMenu() {
        MenuGroup reportsMenu = new MenuGroup("Отчеты", renderer);
        reportsMenu.addItem(1, new CommandLeaf("Ресурсы", new ShowResourcesCommand()));
        reportsMenu.addItem(2, new CommandLeaf("Генераторы", new ShowGeneratorsCommand()));
        reportsMenu.addItem(3, new CommandLeaf("Здания", new ShowBuildingsCommand()));

        return reportsMenu;
    }

    public void start() {
        MenuComponent menu = buildRootMenu();
        while (castle.isAlive()) {
            menu.execute(castle);
        }
    }

    private String formatCost(Map<String, Integer> cost) {
        return cost.entrySet().stream()
                .map(e -> e.getValue() + " " + getResourceLabel(e.getKey()))
                .collect(Collectors.joining(", "));
    }

    private Map<ResourceType, Integer> convertCost(Map<String, Integer> stringCost) {
        return stringCost.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> ResourceType.valueOf(e.getKey()),
                        Map.Entry::getValue
                ));
    }

    private String getResourceLabel(String resourceType) {
        return castle.getConfig().resources().stream()
                .filter(r -> r.type().equals(resourceType))
                .findFirst()
                .map(chypakk.config.ResourceConfig::label)
                .orElse(resourceType.toLowerCase());
    }
}
