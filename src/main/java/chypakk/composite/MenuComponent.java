package chypakk.composite;

import chypakk.model.game.GameState;

public interface MenuComponent {
    String getTitle();

    void execute();
    boolean isVisible();
}
