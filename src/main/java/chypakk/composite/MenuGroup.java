package chypakk.composite;

import chypakk.model.game.GameService;
import chypakk.ui.MenuRender;

import java.util.LinkedHashMap;
import java.util.Map;

public class MenuGroup implements MenuComponent{
    private final String title;
    private final Map<Integer, MenuComponent> items = new LinkedHashMap<>();
    private final MenuRender renderer;
    private final GameService gameService;

    public MenuGroup(String title, MenuRender renderer, GameService gameService) {
        this.title = title;
        this.renderer = renderer;
        this.gameService = gameService;
    }

    public void addItem(int key, MenuComponent component) {
        items.put(key, component);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void execute() {
        while (gameService.isGameActive()) {
            Map<Integer, String> options = buildVisibleOptions();

            if (!title.equals("Главное меню")) {
                options.put(0, "Назад");
            }

            renderer.displayMenu(title, options);
            int choice = renderer.getChoice(options);

            if (choice == 0 && !title.equals("Главное меню")) {
                break;
            }

            MenuComponent selected = getVisibleItem(choice);
            if (selected != null) {
                selected.execute();
            } else {
                renderer.displayMessage("Неверный ввод!");
            }
        }
    }

    @Override
    public boolean isVisible() {
        return items.values().stream().anyMatch(MenuComponent::isVisible);
    }

    private Map<Integer, String> buildVisibleOptions() {
        Map<Integer, String> options = new LinkedHashMap<>();
        int counter = 1;

        for (MenuComponent component : items.values()) {
            if (component.isVisible()) {
                if (component.getTitle().equals("Выход")){
                    options.put(0, component.getTitle());
                } else{
                    options.put(counter++, component.getTitle());
                }
            }
        }

        return options;
    }

    private MenuComponent getVisibleItem(int choice) {
        int counter = 1;
        for (MenuComponent component : items.values()) {
            if (component.isVisible()) {
                if ("Выход".equals(component.getTitle()) && choice == 0) {
                    return component;
                }
                if (counter++ == choice) {
                    return component;
                }
            }
        }
        return null;
    }

}