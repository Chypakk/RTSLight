package chypakk.model.managers;

import chypakk.model.units.Unit;

import java.util.List;

public interface UnitManagement {
    void addUnit(Unit unit);
    void removeUnit(Unit unit);
    void printUnits();
    List<Unit> getUnits();
    List<Unit> getUnits(String type);
}
