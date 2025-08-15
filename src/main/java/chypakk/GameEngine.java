package chypakk;

import chypakk.composite.MenuSystem;
import chypakk.model.Castle;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.GoldMine;

//todo переписать на lanterna
//todo внедрить паттерн Observer и убрать консольные выводы

//todo написать про паттерны в Obsidian
public class GameEngine {

    private final Castle castle;

    public GameEngine() {
        this.castle = new Castle(100);
    }

    public void start() {

        GoldMine goldMine = new GoldMine(5, 10, 70, castle);
        Forest forest = new Forest(2, 5, 100, castle);
        castle.addGenerator(goldMine);
        castle.addGenerator(forest);

        new MenuSystem(castle).start();
    }

}