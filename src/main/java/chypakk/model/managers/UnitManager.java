package chypakk.model.managers;

import chypakk.model.units.Unit;
import chypakk.observer.event.Action;
import chypakk.observer.EventNotifier;
import chypakk.observer.event.UnitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UnitManager implements UnitManagement {
    private final Logger logger = LoggerFactory.getLogger(UnitManager.class);

    private final List<Unit> units = new CopyOnWriteArrayList<>();
    private final EventNotifier eventNotifier;

    public UnitManager(EventNotifier eventNotifier) {
        this.eventNotifier = eventNotifier;
    }

    @Override
    public void addUnit(Unit unit) {
        logger.debug("добавление {}", unit);

        units.add(unit);
        eventNotifier.notifyObservers(new UnitEvent(
                unit.getName(),
                Action.ADDED
        ));
    }

    @Override
    public void removeUnit(Unit unit) {
        logger.debug("удаление {}", unit);

        units.remove(unit);
        eventNotifier.notifyObservers(new UnitEvent(
                unit.getName(),
                Action.REMOVED
        ));
    }

    @Override
    public void printUnits() {
        if (units.isEmpty()) {
            System.out.println("Юнитов пока нет");
            return;
        }
        System.out.println("\nЮниты:");
        for (Unit unit : units) {
            System.out.println(unit);
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
