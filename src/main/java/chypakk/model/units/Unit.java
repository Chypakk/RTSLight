        package chypakk.model.units;

import chypakk.model.strategy.AttackStrategy;

public abstract class Unit {
    private final String name;
    private int health;
    protected final int healthMax;
    private int baseDamage;
    private final AttackStrategy attackStrategy;

    public Unit(String name, int health, int baseDamage, AttackStrategy attackStrategy) {
        this.name = name;
        this.health = health;
        this.healthMax = health;
        this.baseDamage = baseDamage;
        this.attackStrategy = attackStrategy;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public int attack(){
        return attackStrategy.execute(baseDamage);
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }

    public boolean isAlive(){
        return health > 0;
    }

    @Override
    public String toString() {
        return name + " (" + health + "/" + healthMax + ")";
    }
}