package chypakk.composite;

import java.util.function.Supplier;

public class CommandLeaf implements MenuComponent{
    private final String title;

    private final Runnable action;
    private final Supplier<Boolean> visibilitySupplier;

    public CommandLeaf(String title, Runnable action) {
        this(title, action, () -> true);
    }

    public CommandLeaf(String title, Runnable action, Supplier<Boolean> visibilitySupplier) {
        this.title = title;
        this.action = action;
        this.visibilitySupplier = visibilitySupplier;
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void execute() {
        action.run();
    }

    @Override
    public boolean isVisible() {
        return visibilitySupplier.get();
    }
}
