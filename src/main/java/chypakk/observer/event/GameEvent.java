package chypakk.observer.event;

public abstract class GameEvent {
    private final String type;
    private final Action action;

    protected GameEvent(String type, Action action) {
        this.type = type;
        this.action = action;
    }

    public String getType(){
        return type;
    }

    public Action getAction(){
        return action;
    }
}
