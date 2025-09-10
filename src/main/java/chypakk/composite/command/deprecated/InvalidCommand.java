package chypakk.composite.command.deprecated;

import chypakk.composite.command.GameCommand;
import chypakk.model.Castle;

@Deprecated
public class InvalidCommand implements GameCommand {
    @Override
    public void execute(Castle castle) {
        System.out.println("Неверная команда!");
    }
}
