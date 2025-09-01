package chypakk;

import chypakk.model.Castle;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.GoldMine;
import chypakk.ui.ConsoleUI;
import chypakk.ui.LanternaUI;

import java.io.IOException;

//todo переписать на lanterna
//todo внедрить паттерн Observer и убрать консольные выводы

//todo написать про паттерны в Obsidian
public class GameEngine {

    private final Castle castle;
    private final ConsoleUI consoleUI;

    public GameEngine() {
        this.castle = new Castle(100);
        this.consoleUI = new ConsoleUI(castle);
    }

    public void start() {

        GoldMine goldMine = new GoldMine(5, 10, 70, castle);
        Forest forest = new Forest(2, 5, 100, castle);
        castle.addGenerator(goldMine);
        castle.addGenerator(forest);

        consoleUI.start();

//        try {
//            new LanternaUI(castle, menuSystem).start();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }
}