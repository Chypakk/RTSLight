package chypakk.model.managers;

import chypakk.config.ResourceConfig;
import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;
import chypakk.observer.event.Action;
import chypakk.observer.EventNotifier;
import chypakk.observer.event.ResourceEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager implements ResourceManagement {
    private final Map<ResourceType, Resource> resources = new ConcurrentHashMap<>();
    private final EventNotifier eventNotifier;
    private final Object spendMutex = new Object();

    public ResourceManager(List<ResourceConfig> resources, EventNotifier eventNotifier) {
        this.eventNotifier = eventNotifier;
        for (ResourceConfig resourceConfig : resources) {
            ResourceType type = ResourceType.fromType(resourceConfig.type());
            if (type != null) {
                this.resources.put(
                        type,
                        new Resource(ResourceType.valueOf(resourceConfig.type()), resourceConfig.initialAmount())
                );
            }
        }
    }

    @Override
    public void addResource(Resource res) {
        synchronized (spendMutex){
            Resource existing = resources.get(res.getType());
            if (existing != null) {
                existing.addAmount(res.getAmount());
            } else {
                resources.put(res.getType(), res);
            }

            eventNotifier.notifyObservers(new ResourceEvent(
                    res.getType().name(), Action.ADDED, res.getAmount()
            ));
        }
    }

    @Override
    public int getResource(ResourceType type) {
        if (type == null) return 0;

        Resource resource = resources.get(type);
        return resource != null ? resource.getAmount() : 0;
    }

    @Override
    public void removeResource(ResourceType type, int amount) {
        synchronized (spendMutex){

            resources.get(type).removeAmount(amount);

            eventNotifier.notifyObservers(new ResourceEvent(
                    type.name(), Action.REMOVED, amount
            ));
        }
    }

    @Override
    public void printResources() {
        if (resources.isEmpty()) {
            System.out.println("Ресурсов пока нет");
            return;
        }
        for (Resource res : resources.values()) {
            System.out.println(res);
        }
    }

    @Override
    public boolean trySpendResources(Map<ResourceType, Integer> cost) {
        synchronized (spendMutex) {
            for (var entry : cost.entrySet()) {
                ResourceType type = entry.getKey();
                int required = entry.getValue();
                if (resources.get(type).getAmount() < required) {
                    return false;
                }
            }

            for (var entry : cost.entrySet()) {
                removeResource(entry.getKey(), entry.getValue());
            }
        }

        return true;
    }
}
