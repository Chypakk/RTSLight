package chypakk.composite;

import chypakk.model.Castle;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class MenuGroup implements MenuComponent{
    private final String title;
    private final Map<Integer, MenuComponent> items = new LinkedHashMap<>();

    public MenuGroup(String title) {
        this.title = title;
    }

    public void addItem(int key, MenuComponent component) {
        items.put(key, component);
    }

    @Override
    public void execute(Castle castle) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n" + title + ":");
            items.forEach((k, v) -> System.out.println(k + " - " + v.getTitle()));

            if (!title.equals("Главное меню")) System.out.println("0 - Назад");

            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            //if (choice == 0) break;

            MenuComponent selected = items.get(choice);
            if (selected != null) {
                selected.execute(castle);
            } else {
                if (choice == 0) break;
                System.out.println("Неверный ввод!");
            }
        }
    }

    @Override
    public String getTitle() {
        return title;
    }
}
