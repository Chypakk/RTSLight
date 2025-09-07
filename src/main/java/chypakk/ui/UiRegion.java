package chypakk.ui;

public enum UiRegion {
    MENU(0.0, 0.0, 0.5, 1.0),
    GENERATOR_PANEL(0.5, 0.0, 0.5, 0.5),
    RESOURCE_PANEL(0.5, 0.5, 0.5, 0.5);

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
