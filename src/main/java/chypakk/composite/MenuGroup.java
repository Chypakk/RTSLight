package chypakk.composite;

import chypakk.model.Castle;
import chypakk.ui.MenuRender;

import java.util.LinkedHashMap;
import java.util.Map;

public class MenuGroup implements MenuComponent{
    private final String title;
    private final Map<Integer, MenuComponent> items = new LinkedHashMap<>();
    private final MenuRender renderer;

    public MenuGroup(String title, MenuRender renderer) {
        this.title = title;
        this.renderer = renderer;
    }

    public void addItem(int key, MenuComponent component) {
        items.put(key, component);
    }

    @Override
    public void execute(Castle castle) {
        while (true) {

            Map<Integer, String> options = new LinkedHashMap<>();
            items.forEach((k, v) -> options.put(k, v.getTitle()));

            if (!title.equals("Главное меню")) {
                options.put(0, "Назад");
            }

            renderer.displayMenu(title, options);
            int choice = renderer.getChoice(options);

            if (choice == 0 && !title.equals("Главное меню")) {
                break;
            }

            MenuComponent selected = items.get(choice);
            if (selected != null) {
                selected.execute(castle);
            } else {
                renderer.displayMessage("Неверный ввод!");
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }
}