package chypakk.model.resources;

public class Gold extends Resource {
    public Gold(int amount) {
        super(ResourceType.GOLD, amount);
    }

    @Override
    public String toString() {
        return "Тип - 'золото', кол-во: " + amount;
    }
}