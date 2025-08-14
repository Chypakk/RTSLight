package chypakk.command;

import chypakk.model.Castle;

public class InvalidCommand implements GameCommand{
    @Override
    public void execute(Castle castle) {
        System.out.println("Неверная команда!");
    }
}
