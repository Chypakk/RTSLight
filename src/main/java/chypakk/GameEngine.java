package chypakk;

import chypakk.model.Castle;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.GoldMine;
import chypakk.ui.GameUI;
import chypakk.ui.LanternaUI;

import java.io.IOException;

//todo написать про паттерны в Obsidian
public class GameEngine {

    private final Castle castle;
    private final GameUI gameUI;

    public GameEngine() {
        this.castle = new Castle(100);
        try {
            this.gameUI = new LanternaUI(castle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {

        GoldMine goldMine = new GoldMine(5, 10, 70, castle);
        Forest forest = new Forest(2, 5, 100, castle);
        castle.addGenerator(goldMine);
        castle.addGenerator(forest);

        gameUI.start();
    }
}