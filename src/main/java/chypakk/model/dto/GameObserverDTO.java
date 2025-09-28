package chypakk.model.dto;

import chypakk.model.resources.ResourceType;

public interface GameObserverDTO {
    record Message(String text) implements GameObserverDTO {}
    record ResourceChanged(ResourceType type, int amount) implements GameObserverDTO {}
    record BuildingAdded(String name) implements GameObserverDTO {}
    record GeneratorAdded(String type) implements GameObserverDTO {}
    record GeneratorAlmostRemoved(String type) implements GameObserverDTO {}
    record UnitAdded(String type) implements GameObserverDTO {}
}
