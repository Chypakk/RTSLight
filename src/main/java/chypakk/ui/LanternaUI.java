package chypakk.ui;

import chypakk.composite.MenuSystem;
import chypakk.config.*;
import chypakk.model.dto.GameObserverDTO;
import chypakk.model.game.GameService;
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
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class LanternaUI implements GameUI {

    private static final int WIDTH_SCREEN = 100; // 80 - default width screen
    private static final int HEIGHT_SCREEN = 30; // 24 - default height screen

    private final Screen screen;
    private final TextGraphics graphics;
    private final UiLayout uiLayout;

    private final MenuSystem menuSystem;
    private final GameService gameService;

    public LanternaUI(GameService gameService) throws IOException {
        this.gameService = gameService;
        this.menuSystem = new MenuSystem(gameService, this);
        gameService.subscribeToEvents(this::handleGameEventDTO);

        screen = new TerminalScreen(new DefaultTerminalFactory()
                .setInitialTerminalSize(new TerminalSize(WIDTH_SCREEN, HEIGHT_SCREEN))
                .setForceAWTOverSwing(true)
                .setTerminalEmulatorFontConfiguration(
                        SwingTerminalFontConfiguration.newInstance(new Font("Consolas", Font.PLAIN, 14))
                )
                .createTerminal());
        screen.setCursorPosition(null);
        graphics = screen.newTextGraphics();
        this.uiLayout = new UiLayout(screen);
    }

    private void handleGameEventDTO(GameObserverDTO event) {
        try {
            switch (event) {
                case GameObserverDTO.Message msg -> displayMessage(msg.text());
                case GameObserverDTO.ResourceChanged res -> updateResourcePanel();
                case GameObserverDTO.BuildingAdded b -> updateBuildingPanel();
                case GameObserverDTO.GeneratorAdded g -> updateGeneratorPanel();
                case GameObserverDTO.GeneratorAlmostRemoved g -> updateGeneratorPanel();
                case GameObserverDTO.UnitAdded u -> updateUnitPanel();

                default -> throw new IllegalStateException("Unexpected value: " + event);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка обновления UI", e);
        }
    }

    @Override
    public void init() {
        try {
            updateBuildingPanel();
            updateUnitPanel();
            displayMessage("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        try {
            screen.startScreen();

            init();
            menuSystem.start();

            screen.stopScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessage(String message) {
        displayMessage(message);
    }

    @Override
    public void onEvent(GameEvent gameEvent) {
//        try {
//            switch (gameEvent) {
//                case ResourceEvent event -> updateResourcePanel();
//                case GeneratorEvent event -> updateGeneratorPanel();
//                case BuildingEvent event -> updateBuildingPanel();
//                case UnitEvent event -> updateUnitPanel();
//
//                default -> throw new IllegalStateException("Unexpected value: " + gameEvent);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    private void updateUnitPanel() throws IOException {
        uiLayout.renderItemList(
                graphics,
                UiRegion.UNIT_PANEL,
                gameService.getConfig().units(),
                UnitConfig::label,
                config -> gameService.getUnitCount(config.label()),
                config -> 0
        );
    }

    private void updateBuildingPanel() throws IOException {
        uiLayout.renderItemList(
                graphics,
                UiRegion.BUILDING_PANEL,
                gameService.getConfig().buildings(),
                BuildingConfig::label,
                config -> gameService.hasBuilding(config.label())
        );
    }

    private void updateGeneratorPanel() throws IOException {
        uiLayout.renderItemList(
                graphics,
                UiRegion.GENERATOR_PANEL,
                gameService.getConfig().generators(),
                GeneratorConfig::label,
                config -> gameService.getGeneratorCount(config.type()),
                config -> gameService.getGeneratorAlmostRemovedCount(config.type())
        );
    }

    private void updateResourcePanel() throws IOException {
        uiLayout.renderItemList(
                graphics,
                UiRegion.RESOURCE_PANEL,
                gameService.getConfig().resources(),
                ResourceConfig::label,
                config -> gameService.getResourceAmount(config.type()),
                config -> 0
        );
    }

    @Override
    public void displayMenu(String title, Map<Integer, String> options) {
        uiLayout.clear(graphics, UiRegion.MENU);
        uiLayout.drawBox(graphics, UiRegion.MENU);

        Rectangle bounds = uiLayout.getBounds(UiRegion.MENU);
        int x = (int) (bounds.getX() + 2);
        int y = (int) (bounds.getY() + 1);
        int maxWidth = (int) bounds.getWidth();

        String header = "[ " + title + " ]";
        int headerX = (int) (bounds.getX() + (bounds.getWidth() / 2) - (header.length() / 2));
        graphics.putString(headerX, (int) bounds.getY(), header);

        for (var entry : options.entrySet()) {
            String text = entry.getKey() + " - " + entry.getValue();
            List<String> lines = wrapText(text, maxWidth);

            for (String line : lines) {
                if (y >= bounds.getY() + bounds.getHeight() - 1) {
                    break;
                }
                graphics.putString(x, y++, line);
            }
        }

        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty() || maxWidth <= 0) {
            return lines;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (word.length() > maxWidth) {
                flushLine(currentLine, lines);
                for (int i = 0; i < word.length(); i += maxWidth) {
                    int end = Math.min(i + maxWidth, word.length());
                    lines.add(word.substring(i, end));
                }
                continue;
            }

            if (currentLine.isEmpty()) {
                currentLine.append(word);
            } else if (currentLine.length() + word.length() + 1 <= maxWidth) {
                currentLine.append(" ").append(word);
            } else {
                flushLine(currentLine, lines);
                currentLine.append(word);
            }
        }

        flushLine(currentLine, lines);
        return lines;
    }

    private void flushLine(StringBuilder line, List<String> lines) {
        if (!line.isEmpty()) {
            lines.add(line.toString());
            line.setLength(0);
        }
    }

    //todo переделать
    @Override
    public int getChoice(Map<Integer, String> options) {
        try {
            KeyStroke key = screen.readInput();
            if (key != null) {
                if (key.getKeyType() == KeyType.F1) return 0;

                Character input = key.getCharacter();
                if (input != null) {
                    switch (input) {
                        case '1':
                            return 1;
                        case '2':
                            return 2;
                        case '3':
                            return 3;
                        case '0':
                            return 0;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return -1;
    }

    @Override
    public void displayMessage(String message) {
        uiLayout.clear(graphics, UiRegion.CHAT_PANEL);
        uiLayout.drawBox(graphics, UiRegion.CHAT_PANEL);

        Rectangle bounds = uiLayout.getBounds(UiRegion.CHAT_PANEL);
        int x = (int) (bounds.getX() + 2);
        int y = (int) (bounds.getY() + 1);

        List<String> messages = gameService.getGameLogMessages();
        int maxLines = (int) bounds.getHeight() - 2;
        int startIndex = Math.max(0, messages.size() - maxLines);

        for (int i = startIndex; i < messages.size(); i++) {
            if (y >= bounds.getY() + bounds.getHeight() - 1) break;

            String msg = messages.get(i);
            if (msg.length() > bounds.getWidth() - 4) {
                msg = msg.substring(0, (int) bounds.getWidth() - 7) + "...";
            }
            graphics.putString(x, y++, msg);
        }

        try {
            screen.refresh();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
