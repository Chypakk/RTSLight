package chypakk.model.managers;

import chypakk.model.units.Unit;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UnitManager implements UnitManagement {
    private final List<Unit> units = new CopyOnWriteArrayList<>();

    @Override
    public void addUnit(Unit unit) {
        synchronized (units) {
            units.add(unit);
        }
    }

    @Override
    public void removeUnit(Unit unit) {
        synchronized (units) {
            units.remove(unit);
        }
    }

    @Override
    public void printUnits() {
        synchronized (units) {
            if (units.isEmpty()) {
                System.out.println("Юнитов пока нет");
                return;
            }
            System.out.println("\nЮниты:");
            for (Unit unit : units) {
                System.out.println(unit);
            }
        }
    }

    @Override
    public List<Unit> getUnits() {
        return units;
    }

    @Override
    public List<Unit> getUnits(String type) {
        return units.stream().filter(unit -> unit.getName().equals(type)).toList();
    }
}
