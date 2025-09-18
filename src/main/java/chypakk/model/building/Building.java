package chypakk.model.building;

public abstract class Building {
    protected final String name;
    protected int health;
    protected final int maxHealth;

    public Building(String name, int health) {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
    }

    public String getName() {
        return name;
    }

    public void takeDamage(int damage){
        health -= damage;
    }

    public boolean isAlive(){
        return health > 0;
    }
}
