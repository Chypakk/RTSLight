package chypakk.model.units;

import chypakk.model.units.strategy.MeleeStrategy;

public class Soldier extends Unit {
    public Soldier(String name, int health, int baseDamage) {
        super(name, health, baseDamage, new MeleeStrategy());
    }
}