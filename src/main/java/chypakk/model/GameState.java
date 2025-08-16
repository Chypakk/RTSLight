package chypakk.model;

import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public record GameState(Map<ResourceType, Resource> resources, List<String> generators, List<String> buildings) {
    public GameState(Map<ResourceType, Resource> resources,
                     List<String> generators,
                     List<String> buildings) {
        this.resources = new ConcurrentHashMap<>(resources);
        this.generators = new CopyOnWriteArrayList<>(generators);
        this.buildings = new CopyOnWriteArrayList<>(buildings);
    }

    @Override
    public Map<ResourceType, Resource> resources() {
        return Collections.unmodifiableMap(resources);
    }

    @Override
    public List<String> generators() {
        return Collections.unmodifiableList(generators);
    }

    @Override
    public List<String> buildings() {
        return Collections.unmodifiableList(buildings);
    }
}
