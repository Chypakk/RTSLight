package chypakk.composite;

import chypakk.composite.command.*;
import chypakk.model.Castle;

public class MenuSystem {
    private final Castle castle;

    public MenuSystem(Castle castle) {
        this.castle = castle;
    }

    private MenuComponent buildRootMenu() {

        MenuGroup generatorsMenu = new MenuGroup("Генераторы");
        generatorsMenu.addItem(1, new CommandLeaf("Добавить золотую шахту (50 золота, 60 дерева)", new AddGoldMineCommand()));
        generatorsMenu.addItem(2, new CommandLeaf("Добавить лес (70 дерева)", new AddForestCommand()));

        MenuGroup buildingsMenu = new MenuGroup("Здания");
        buildingsMenu.addItem(1, new CommandLeaf("Добавить рынок (10 золота, 20 дерева)", new AddMarketplaceCommand()));

        MenuGroup buildMenu = new MenuGroup("Построить");
        buildMenu.addItem(1, generatorsMenu);
        buildMenu.addItem(2, buildingsMenu);

        MenuGroup reportsMenu = new MenuGroup("Отчеты");
        reportsMenu.addItem(1, new CommandLeaf("Ресурсы", new ShowResourcesCommand()));
        reportsMenu.addItem(2, new CommandLeaf("Генераторы", new ShowGeneratorsCommand()));
        reportsMenu.addItem(3, new CommandLeaf("Здания", new ShowBuildingsCommand()));

        MenuGroup marketMenu = new MenuGroup("Рынок");

        marketMenu.addItem(1, new CommandLeaf("Обменять 10 дерева на 5 золота", new ExchangeWoodCommand()));
        marketMenu.addItem(2, new CommandLeaf("Обменять 5 золота на 10 дерева", new ExchangeGoldCommand()));

        MenuGroup buildingsUseMenu = new MenuGroup("Использовать");
        buildingsUseMenu.addItem(1, marketMenu);

        MenuGroup rootMenu = new MenuGroup("Главное меню");
        rootMenu.addItem(1, buildMenu);
        rootMenu.addItem(2, buildingsUseMenu);
        rootMenu.addItem(3, reportsMenu);
        rootMenu.addItem(0, new CommandLeaf("Выход", new ExitCommand()));

        return rootMenu;
    }

    //todo внедрить создание юнитов
    public void start() {
        MenuComponent rootMenu = buildRootMenu();
        while (castle.isAlive()) {
            rootMenu.execute(castle);
        }
    }
}
