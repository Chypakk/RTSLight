package chypakk.model.managers;

import chypakk.model.units.Unit;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UnitManager {
    private final List<Unit> units = new CopyOnWriteArrayList<>();

    public void addUnit(Unit unit) {
        synchronized (units) {
            units.add(unit);
        }
    }

    public void removeUnit(Unit unit) {
        synchronized (units) {
            units.remove(unit);
        }
    }

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

    public List<Unit> getUnits() {
        return units;
    }

    public List<Unit> getUnits(String type) {
        return units.stream().filter(unit -> unit.getName().equals(type)).toList();
    }
}
