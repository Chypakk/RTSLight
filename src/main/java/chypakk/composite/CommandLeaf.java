package chypakk.composite;

import chypakk.composite.command.GameCommand;
import chypakk.model.GameState;

import java.util.function.Predicate;

public class CommandLeaf implements MenuComponent{
    private final String title;
    private final GameCommand command;
    private final Predicate<GameState> visibilityCondition;

    public CommandLeaf(String title, GameCommand command) {
        this(title, command, castle -> true); // По умолчанию видим всегда
    }

    public CommandLeaf(String title, GameCommand command, Predicate<GameState> visibilityCondition) {
        this.title = title;
        this.command = command;
        this.visibilityCondition = visibilityCondition;
    }

    @Override
    public void execute(GameState castle) {
        command.execute(castle);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isVisible(GameState castle) {
        return visibilityCondition.test(castle);
    }
}
