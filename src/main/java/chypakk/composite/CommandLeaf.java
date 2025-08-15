package chypakk.composite;

import chypakk.composite.command.GameCommand;
import chypakk.model.Castle;

public class CommandLeaf implements MenuComponent{
    private final String title;
    private final GameCommand command;

    public CommandLeaf(String title, GameCommand command) {
        this.title = title;
        this.command = command;
    }

    @Override
    public void execute(Castle castle) {
        command.execute(castle);
    }

    @Override
    public String getTitle() {
        return title;
    }
}
