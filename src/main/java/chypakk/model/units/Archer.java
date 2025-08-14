package chypakk.model.units;

import chypakk.model.strategy.RangeStrategy;

public class Archer extends Unit{
    public Archer(String name, int health, int baseDamage) {
        super(name, health, baseDamage, new RangeStrategy());
    }
}