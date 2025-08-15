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

        MenuGroup reportsMenu = new MenuGroup("Отчеты");
        reportsMenu.addItem(1, new CommandLeaf("Ресурсы", new ShowResourcesCommand()));
        reportsMenu.addItem(2, new CommandLeaf("Генераторы", new ShowGeneratorsCommand()));


        MenuGroup rootMenu = new MenuGroup("Главное меню");
        rootMenu.addItem(1, generatorsMenu);
        rootMenu.addItem(2, reportsMenu);
        rootMenu.addItem(0, new CommandLeaf("Выход", new ExitCommand()));

        return rootMenu;
    }

    //todo внедрить создание зданий
    //todo внедрить создание юнитов
    public void start() {
        MenuComponent rootMenu = buildRootMenu();
        while (castle.isAlive()) {
            rootMenu.execute(castle);
        }
    }
}
