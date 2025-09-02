package chypakk.ui;

import chypakk.composite.MenuSystem;
import chypakk.model.Castle;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.generator.ResourceGenerator;
import chypakk.observer.event.BuildingEvent;
import chypakk.observer.event.GameEvent;
import chypakk.observer.event.GeneratorEvent;
import chypakk.observer.event.ResourceEvent;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LanternaUI implements GameUI {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 24;

    private final Screen screen;
    private final TextGraphics graphics;

    private final MenuSystem menuSystem;
    private final Castle castle;

    public LanternaUI(Castle castle) throws IOException {
        this.castle = castle;
        this.menuSystem = new MenuSystem(castle, this);
        castle.addObserver(this);

        screen = new TerminalScreen(new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(WIDTH, HEIGHT))
                .setForceAWTOverSwing(true)
                .setTerminalEmulatorFontConfiguration(
                        SwingTerminalFontConfiguration.newInstance(new Font("Consolas", Font.PLAIN, 14))
                )
                .createTerminal());
        screen.setCursorPosition(null);
        graphics = screen.newTextGraphics();
    }

    @Override
    public void initialize() {

    }

    @Override
    public void start() {
        try {

            screen.startScreen();

            while (castle.isAlive()) {
                menuSystem.start();
            }

            screen.stopScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onEvent(GameEvent gameEvent) {
        try {
            switch (gameEvent) {
                case ResourceEvent event -> handleResourceEvent(event);
                case GeneratorEvent event -> handleGeneratorEvent(event);
                case BuildingEvent event -> handleBuildingEvent(event);

                default -> throw new IllegalStateException("Unexpected value: " + gameEvent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void handleBuildingEvent(BuildingEvent event) {
    }

    private void handleGeneratorEvent(GeneratorEvent event) throws IOException {

        switch (event.getType()){
            case "GoldMine" -> {
                List<ResourceGenerator> generators = castle.getGenerators(event.getType());
                String forestText = "шахт: " + generators.size();
                graphics.putString((WIDTH / 2) + 15, 0,forestText);
                graphics.putString((WIDTH / 2) + 15 + forestText.length(), 1, " ".repeat(2));
            }
            case "Forest" -> {
                List<ResourceGenerator> generators = castle.getGenerators(event.getType());
                String forestText = "лесов: " + generators.size();
                graphics.putString((WIDTH / 2) + 14, 1, forestText);
                graphics.putString((WIDTH / 2) + 14 + forestText.length(), 1, " ".repeat(2));
            }
        }

        screen.refresh();
    }

    private void handleResourceEvent(ResourceEvent event) throws IOException {

        switch (event.getType()){

            case "GOLD" -> {
                String goldText = "золото: ";
                int gold = castle.getResource(ResourceType.GOLD);
                goldText = gold >= 999 ? goldText + "999+" : goldText + gold;
                graphics.putString((WIDTH / 2) + 25, 0, goldText);
                graphics.putString((WIDTH / 2) + 25 + goldText.length(), 0, " ".repeat(10));
            }
            case "WOOD" -> {
                String woodText = "дерево: ";
                int wood = castle.getResource(ResourceType.WOOD);
                woodText = wood >= 999 ? woodText + "999+" : woodText + wood;
                graphics.putString((WIDTH / 2) + 25, 1, woodText);
                graphics.putString((WIDTH / 2) + 25 + woodText.length(), 1, " ".repeat(10));
            }

        }

        screen.refresh();
    }

    @Override
    public void displayMenu(String title, Map<Integer, String> options) {
        //screen.clear();
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        int x = 0;
        for (var entry : options.entrySet()) {
            graphics.putString(1, x, entry.getKey() + " - " + entry.getValue());
            x++;
        }

        try {

            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getChoice(Map<Integer, String> options) {
        try {
            KeyStroke key = screen.readInput();
            if (key.getKeyType() == KeyType.F1) return 0;

            Character input = key.getCharacter();
            return switch (input) {
                case '1' -> 1;
                case '2' -> 2;
                case '3' -> 3;
                default -> 0;
            };


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void displayMessage(String message) {

    }
}
