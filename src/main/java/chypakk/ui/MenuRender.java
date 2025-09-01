package chypakk.ui;

import java.util.Map;

public interface MenuRender {
    void displayMenu(String title, Map<Integer, String> options);
    int getChoice(Map<Integer, String> options);
    void displayMessage(String message);
}
