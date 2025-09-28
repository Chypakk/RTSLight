package chypakk.ui;

import chypakk.composite.MenuSystem;
import chypakk.model.game.GameService;
import chypakk.model.game.GameServiceImpl;
import chypakk.model.game.GameState;
import chypakk.observer.event.BuildingEvent;
import chypakk.observer.event.GameEvent;
import chypakk.observer.event.GeneratorEvent;
import chypakk.observer.event.ResourceEvent;

import java.util.Map;
import java.util.Scanner;

public class ConsoleUI implements GameUI {
    private final GameState castle;
    private final GameService gameService;
    private final MenuSystem menuSystem;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(GameService gameService) {
        this.gameService = gameService;
        this.castle = ((GameServiceImpl) gameService).getGameState();
        this.menuSystem = new MenuSystem(gameService, this);
        castle.addObserver(this);
    }

    @Override
    public void start() {
        while (gameService.isGameActive()) {
            menuSystem.start();
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void onMessage(String message) {
        System.out.println("[СООБЩЕНИЕ] " + message);
    }

    @Override
    public void onEvent(GameEvent gameEvent) {
        switch (gameEvent) {
            case GeneratorEvent event -> handleGeneratorEvent(event);
            case ResourceEvent event -> handleResourceEvent(event);
            case BuildingEvent event -> handleBuildingEvent(event);

            default -> throw new IllegalStateException("Unexpected value: " + gameEvent);
        }
    }

    private void handleBuildingEvent(BuildingEvent event) {
        System.out.println("[СООБЩЕНИЕ] " + event.getType()  + " " + event.getAction());
    }

    private void handleResourceEvent(ResourceEvent event) {
        System.out.println("[СООБЩЕНИЕ] " + event.getType()  + " " + event.getAction() + " " + event.getAmount());
    }

    private void handleGeneratorEvent(GeneratorEvent event) {
        System.out.println("[СООБЩЕНИЕ] " + event.getAction() + " " + event.getType());
    }

    @Override
    public void displayMenu(String title, Map<Integer, String> options) {
        System.out.println("\n" + title + ":");
        options.forEach((key, value) -> System.out.println(key + " - " + value));
    }

    @Override
    public int getChoice(Map<Integer, String> options) {
        System.out.print("Ваш выбор: ");
        return scanner.nextInt();
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
