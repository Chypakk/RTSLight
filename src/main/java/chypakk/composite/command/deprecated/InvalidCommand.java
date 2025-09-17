package chypakk.composite.command.deprecated;

import chypakk.composite.command.GameCommand;
import chypakk.model.game.GameState;

@Deprecated
public class InvalidCommand implements GameCommand {
    @Override
    public void execute(GameState castle) {
        System.out.println("Неверная команда!");
    }
}
