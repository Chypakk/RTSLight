package chypakk.composite.command;

import chypakk.model.Castle;

public interface GameCommand {
    void execute(Castle castle);
}
