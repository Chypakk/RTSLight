package chypakk.observer.event;

public class GeneratorEvent implements GameEvent {
    public enum Action {ADDED, REMOVED}

    private final String generatorType;
    private final Action action;

    public GeneratorEvent(String generatorType, Action action) {
        this.generatorType = generatorType;
        this.action = action;
    }

    public String getGeneratorType() {
        return generatorType;
    }

    public Action getAction() {
        return action;
    }
}
