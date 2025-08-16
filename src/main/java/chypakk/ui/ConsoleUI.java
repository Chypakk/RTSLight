package chypakk.ui;

import chypakk.composite.MenuSystem;
import chypakk.model.Castle;
import chypakk.model.GameState;

public class ConsoleUI extends GameUI {

    private final MenuSystem menuSystem;

    public ConsoleUI(Castle castle, MenuSystem menuSystem) {
        super(castle);
        this.menuSystem = menuSystem;
    }

    @Override
    public void start() {
        while (castle.isAlive()) {
            menuSystem.start();
        }
    }

    @Override
    public void render(GameState state) {
        System.out.println("\n=== ОБНОВЛЕНИЕ СОСТОЯНИЯ ===");
        System.out.println("Ресурсы: " + state.resources());
        System.out.println("Генераторы: " + state.generators());
        System.out.println("Здания: " + state.buildings());
        System.out.println("============================\n");
    }

}
