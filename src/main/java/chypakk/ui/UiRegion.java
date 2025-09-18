package chypakk.ui;

public enum UiRegion {
    MENU(0.0, 0.0, 0.5, 0.6),
    RESOURCE_PANEL(0.5, 0.5, 0.5, 0.5),
    GENERATOR_PANEL(0.5, 0.0, 0.5, 0.5),
    BUILDING_PANEL(0.0, 0.6, 0.25, 0.2),
    UNIT_PANEL(0.25, 0.6, 0.25, 0.2),
    CHAT_PANEL(0.0, 0.8, 0.5, 0.2);

    private final double left;
    private final double top;
    private final double width;
    private final double height;

    UiRegion(double left, double top, double width, double height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public double getLeft(){
        return left;
    }

    public double getTop() {
        return top;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
