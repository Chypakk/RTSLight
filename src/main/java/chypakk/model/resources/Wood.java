package chypakk.model.resources;

public class Wood extends Resource{
    public Wood(int amount) {
        super(ResourceType.WOOD, amount);
    }

    @Override
    public String toString() {
        return "Тип - 'дерево', кол-во: " + amount;
    }
}