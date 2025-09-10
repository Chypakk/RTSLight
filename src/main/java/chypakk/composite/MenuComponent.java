package chypakk.composite;

import chypakk.model.Castle;

public interface MenuComponent {
    void execute(Castle castle);
    String getTitle();
    boolean isVisible(Castle castle);
}
