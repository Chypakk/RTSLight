package chypakk.model.resources;

public class Resource {
    private final ResourceType type;
    protected int amount;

    public Resource(ResourceType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public ResourceType getType(){
        return type;
    }

    public int getAmount(){
        return amount;
    }

    public void addAmount(int amount){
        this.amount += amount;
    }

    public void removeAmount(int amount){
        this.amount -= amount;
    }
}