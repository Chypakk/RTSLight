package chypakk.model.units.strategy;

public class MeleeStrategy implements AttackStrategy{
    @Override
    public int execute(int baseDamage) {
        return baseDamage;
    }
}