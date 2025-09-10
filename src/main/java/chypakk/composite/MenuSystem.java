package chypakk.composite;

import chypakk.composite.command.*;
import chypakk.model.Castle;
import chypakk.model.building.Barracks;
import chypakk.model.building.Marketplace;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.GoldMine;
import chypakk.ui.MenuRender;

import java.util.Map;

import static chypakk.model.resources.ResourceType.GOLD;
import static chypakk.model.resources.ResourceType.WOOD;

public class MenuSystem {
    private final Castle castle;
    private final MenuRender renderer;

    public MenuSystem(Castle castle, MenuRender renderer) {
        this.castle = castle;
        this.renderer = renderer;
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
        generatorsMenu.addItem(1, new CommandLeaf("Добавить шахту (50 золота, 60 дерева)", new AddGeneratorCommand(
                () -> new GoldMine(2, 10, 70, castle),
                Map.of(
                        GOLD, 50, WOOD, 60
                )
        )));
        generatorsMenu.addItem(2, new CommandLeaf("Добавить лес (70 дерева)", new AddGeneratorCommand(
                () -> new Forest(1, 5, 100, castle),
                Map.of(
                        WOOD, 70
                )
        )));

        MenuGroup construction = new MenuGroup("Здания", renderer);
        construction.addItem(1, new CommandLeaf("Добавить рынок (10 золота, 20 дерева)",
                new AddBuildingCommand(
                        new Marketplace(),
                        Map.of(
                                GOLD, 10, WOOD, 20
                        )
                ),
                castle -> !castle.haveBuilding("Рынок")
        ));
        construction.addItem(2, new CommandLeaf("Добавить казармы (50 золота, 50 дерева)",
                new AddBuildingCommand(
                        new Barracks(),
                        Map.of(
                                GOLD, 50, WOOD, 50
                        )
                ),
                castle -> !castle.haveBuilding("Казармы")
        ));

        MenuGroup buildMenu = new MenuGroup("Построить", renderer);
        buildMenu.addItem(1, generatorsMenu);
        buildMenu.addItem(2, construction);

        return buildMenu;
    }

    private MenuComponent buildUseBuildingMenu() {
        MenuGroup buildingsUseMenu = new MenuGroup("Здания", renderer);
        MenuGroup marketMenu = new MenuGroup("Рынок", renderer);

        marketMenu.addItem(1, new CommandLeaf(
                "Обменять 10 дерева на 5 золота",
                new ExchangeResourceCommand(WOOD, 10, GOLD, 5),
                castle -> castle.haveBuilding("Рынок")
        ));
        marketMenu.addItem(2, new CommandLeaf(
                "Обменять 5 золота на 10 дерева",
                new ExchangeResourceCommand(GOLD, 5, WOOD, 10),
                castle -> castle.haveBuilding("Рынок")
        ));

        buildingsUseMenu.addItem(1, marketMenu);

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
}
