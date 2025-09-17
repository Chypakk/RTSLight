package chypakk.composite;

import chypakk.model.GameState;

public interface MenuComponent {
    void execute(GameState castle);
    String getTitle();
    boolean isVisible(GameState castle);
}
