package chypakk.composite;

import chypakk.composite.command.*;
import chypakk.model.Castle;

import static chypakk.model.resources.ResourceType.GOLD;
import static chypakk.model.resources.ResourceType.WOOD;

public class MenuSystem {
    private final Castle castle;

    public MenuSystem(Castle castle) {
        this.castle = castle;
    }

    private MenuComponent buildRootMenu() {

        MenuGroup rootMenu = new MenuGroup("Главное меню");
        rootMenu.addItem(1, buildConstructMenu());
        rootMenu.addItem(2, buildUseBuildingMenu());
        rootMenu.addItem(3, buildReportsMenu());
        rootMenu.addItem(0, new CommandLeaf("Выход", new ExitCommand()));

        return rootMenu;
    }

    private MenuComponent buildConstructMenu() {
        MenuGroup generatorsMenu = new MenuGroup("Генераторы");
        generatorsMenu.addItem(1, new CommandLeaf("Добавить золотую шахту (50 золота, 60 дерева)", new AddGoldMineCommand()));
        generatorsMenu.addItem(2, new CommandLeaf("Добавить лес (70 дерева)", new AddForestCommand()));

        MenuGroup construction = new MenuGroup("Здания");
        construction.addItem(1, new CommandLeaf("Добавить рынок (10 золота, 20 дерева)", new AddMarketplaceCommand()));

        MenuGroup buildMenu = new MenuGroup("Построить");
        buildMenu.addItem(1, generatorsMenu);
        buildMenu.addItem(2, construction);

        return buildMenu;
    }

    private MenuComponent buildUseBuildingMenu(){
        MenuGroup buildingsUseMenu = new MenuGroup("Использовать");

        MenuGroup marketMenu = new MenuGroup("Рынок");

        marketMenu.addItem(1, new CommandLeaf("Обменять 10 дерева на 5 золота", new ExchangeResourceCommand(WOOD, 10, GOLD, 5)));
        marketMenu.addItem(2, new CommandLeaf("Обменять 5 золота на 10 дерева", new ExchangeResourceCommand(GOLD, 5, WOOD, 10)));
        buildingsUseMenu.addItem(1, marketMenu);

        return buildingsUseMenu;
    }

    private MenuComponent buildReportsMenu(){
        MenuGroup reportsMenu = new MenuGroup("Отчеты");
        reportsMenu.addItem(1, new CommandLeaf("Ресурсы", new ShowResourcesCommand()));
        reportsMenu.addItem(2, new CommandLeaf("Генераторы", new ShowGeneratorsCommand()));
        reportsMenu.addItem(3, new CommandLeaf("Здания", new ShowBuildingsCommand()));

        return reportsMenu;
    }

    //todo внедрить создание юнитов
    public void start() {
        MenuComponent rootMenu = buildRootMenu();
        while (castle.isAlive()) {
            rootMenu.execute(castle);
        }
    }
}
