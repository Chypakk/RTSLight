package chypakk.command.factory;

import chypakk.command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private final Map<Integer, GameCommand> commands = new HashMap<>();

    public CommandFactory() {
        commands.put(1, new AddGoldMineCommand());
        commands.put(2, new AddForestCommand());
        commands.put(3, new ShowResourcesCommand());
        commands.put(4, new ShowGeneratorsCommand());
        commands.put(0, new ExitCommand());
    }

    public GameCommand getCommand(int choice){
        return commands.getOrDefault(choice, new InvalidCommand());
    }
}
