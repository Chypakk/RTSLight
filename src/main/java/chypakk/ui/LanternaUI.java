package chypakk.ui;

import chypakk.composite.MenuSystem;
import chypakk.model.Castle;
import chypakk.model.GeneratorDisplayConfig;
import chypakk.model.ResourceDisplayConfig;
import chypakk.model.resources.ResourceType;
import chypakk.observer.event.*;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class LanternaUI implements GameUI {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 24;

    private final Screen screen;
    private final TextGraphics graphics;
    private final UiLayout uiLayout;

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
        this.uiLayout = new UiLayout(screen);
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
    public void onMessage(String message) {
//        switch (event.getType()){
//            case "GoldMine" -> {
//                List<ResourceGenerator> generators = castle.getGenerators(event.getType());
//                String forestText = "шахт: " + generators.size();
//                graphics.putString((WIDTH / 2) + 15, 0,forestText);
//                graphics.putString((WIDTH / 2) + 15 + forestText.length(), 0, " ".repeat(2));
//            }
//            case "Forest" -> {
//                List<ResourceGenerator> generators = castle.getGenerators(event.getType());
//
//                if (event.getAction() == Action.ALMOST_REMOVED){
//                    almostRemovedForestCount++;
//                } else if (event.getAction() == Action.REMOVED){
//                    almostRemovedForestCount--;
//                }
//                String forestText = "лесов: " + generators.size();
//                int totalLength = forestText.length();
//
//                graphics.setForegroundColor(TextColor.ANSI.WHITE);
//                graphics.putString((WIDTH / 2) + 10, 1, forestText);
//
//                if (almostRemovedForestCount > 0){
//                    String forestCount = " (" + almostRemovedForestCount + ")";
//                    graphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
//                    graphics.putString((WIDTH / 2) + 10 + totalLength, 1, forestCount);
//                    totalLength += forestCount.length();
//                }
//
//                graphics.putString((WIDTH / 2) + 10 + totalLength, 1, " ".repeat(5));
//            }
//        }
    }

    @Override
    public void onEvent(GameEvent gameEvent) {
        try {
            switch (gameEvent) {
                case ResourceEvent event -> updateResourcePanel();
                case GeneratorEvent event -> updateGeneratorPanel();
                case BuildingEvent event -> handleBuildingEvent(event);

                default -> throw new IllegalStateException("Unexpected value: " + gameEvent);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateGeneratorPanel() throws IOException {
        uiLayout.renderItemList(
                graphics,
                UiRegion.GENERATOR_PANEL,
                castle.getGeneratorDisplayConfigs(),
                GeneratorDisplayConfig::label,
                config -> castle.getGenerators(config.type()).size(),
                config -> castle.getAlmostRemovedCount(config.type())
        );
    }

    private void updateResourcePanel() throws IOException {
        uiLayout.renderItemList(
                graphics,
                UiRegion.RESOURCE_PANEL,
                castle.getResourceDisplayConfigs(),
                ResourceDisplayConfig::label,
                config -> castle.getResource(ResourceType.fromType(config.type())),
                config -> 0
        );
    }

    private void handleBuildingEvent(BuildingEvent event) {
    }

    @Override
    public void displayMenu(String title, Map<Integer, String> options) {
        uiLayout.clear(graphics, UiRegion.MENU);
        uiLayout.drawBox(graphics, UiRegion.MENU);

        Rectangle bounds = uiLayout.getBounds(UiRegion.MENU);
        int x = (int) (bounds.getX() + 2);
        int y = (int) (bounds.getY() + 1);

        String header = "[ " + title + " ]";
        int headerX = (int) (bounds.getX() + (bounds.getWidth() / 2) - (header.length() / 2));
        graphics.putString(headerX, (int) bounds.getY(), header);

        for (var entry : options.entrySet()) {
            graphics.putString(x, y++, entry.getKey() + " - " + entry.getValue());
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
