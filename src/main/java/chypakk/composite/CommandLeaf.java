package chypakk.composite;

import chypakk.composite.command.GameCommand;
import chypakk.model.Castle;

import java.util.function.Predicate;

public class CommandLeaf implements MenuComponent{
    private final String title;
    private final GameCommand command;
    private final Predicate<Castle> visibilityCondition;

    public CommandLeaf(String title, GameCommand command) {
        this(title, command, castle -> true); // По умолчанию видим всегда
    }

    public CommandLeaf(String title, GameCommand command, Predicate<Castle> visibilityCondition) {
        this.title = title;
        this.command = command;
        this.visibilityCondition = visibilityCondition;
    }

    @Override
    public void execute(Castle castle) {
        command.execute(castle);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public boolean isVisible(Castle castle) {
        return visibilityCondition.test(castle);
    }
}
