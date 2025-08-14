package org.chypakk.model.resources;

public class Resource {
    private final String type;
    protected int amount;

    public Resource(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType(){
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
