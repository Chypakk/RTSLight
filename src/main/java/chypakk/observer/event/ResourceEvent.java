package chypakk.observer.event;

public class ResourceEvent extends GameEvent {

    private final int amount;

    public ResourceEvent(String type, Action action, int amount) {
        super(type, action);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
