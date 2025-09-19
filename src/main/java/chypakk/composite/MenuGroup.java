package chypakk.composite;

import chypakk.model.game.GameState;
import chypakk.observer.MessageNotifier;
import chypakk.ui.MenuRender;

import java.util.LinkedHashMap;
import java.util.Map;

public class MenuGroup implements MenuComponent{
    private final String title;
    private final Map<Integer, MenuComponent> items = new LinkedHashMap<>();
    private final MenuRender renderer;
    private final MessageNotifier messageNotifier;

    public MenuGroup(String title, MenuRender renderer, MessageNotifier messageNotifier) {
        this.title = title;
        this.renderer = renderer;
        this.messageNotifier = messageNotifier;
    }

    public void addItem(int key, MenuComponent component) {
        items.put(key, component);
    }

    @Override
    public void execute(GameState castle) {
        while (castle.isGameActive()) {
            Map<Integer, String> options = buildVisibleOptions(castle);

            if (!title.equals("Главное меню")) {
                options.put(0, "Назад");
            }

            renderer.displayMenu(title, options);
            int choice = renderer.getChoice(options);

            if (choice == 0 && !title.equals("Главное меню")) {
                break;
            }

            MenuComponent selected = getVisibleItem(castle, choice);
            if (selected != null) {
                selected.execute(castle);
            } else {
                messageNotifier.sendMessage("Неверный ввод!");
            }
        }
    }

    private Map<Integer, String> buildVisibleOptions(GameState castle) {
        Map<Integer, String> options = new LinkedHashMap<>();
        int counter = 1;

        for (MenuComponent component : items.values()) {
            if (component.isVisible(castle)) {
                if (component.getTitle().equals("Выход")){
                    options.put(0, component.getTitle());

                } else{
                    options.put(counter++, component.getTitle());

                }
            }
        }

        return options;
    }

    private MenuComponent getVisibleItem(GameState castle, int choice) {
        int counter = 1;
        for (MenuComponent component : items.values()) {
            if (component.isVisible(castle)) {
                if (counter++ == choice) {
                    return component;
                }
                if (component.getTitle().equals("Выход") && choice == 0){
                    return component;
                }
            }
        }
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isVisible(GameState castle) {
        return items.values().stream().anyMatch(item -> item.isVisible(castle));
    }
}