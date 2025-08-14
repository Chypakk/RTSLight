package org.chypakk.model.units;

import org.chypakk.model.strategy.RangeStrategy;

public class Archer extends Unit{
    public Archer(String name, int health, int baseDamage) {
        super(name, health, baseDamage, new RangeStrategy());
    }
}
