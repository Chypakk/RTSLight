package chypakk.model.units.strategy;

public class RangeStrategy implements AttackStrategy{
    private final double CRIT_CHANCE = 0.5;
    private final double CRIT_MULTIPLIER = 1.5;

    @Override
    public int execute(int baseDamage) {
        if (Math.random() < CRIT_CHANCE){
            return (int) (baseDamage * CRIT_MULTIPLIER);
        }
        return baseDamage;
    }
}