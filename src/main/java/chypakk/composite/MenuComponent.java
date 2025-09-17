package chypakk.composite;

import chypakk.model.game.GameState;

public interface MenuComponent {
    void execute(GameState castle);
    String getTitle();
    boolean isVisible(GameState castle);
}
