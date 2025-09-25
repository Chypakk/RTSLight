package chypakk.model.managers;

import chypakk.config.ResourceConfig;
import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;
import chypakk.model.resources.ResourcesBuilder;
import chypakk.observer.event.Action;
import chypakk.observer.EventNotifier;
import chypakk.observer.event.ResourceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager implements ResourceManagement {
    private final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

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
                        new Resource(ResourceType.fromType(resourceConfig.type()), resourceConfig.initialAmount())
                );
            }
        }
    }

    @Override
    public void addResource(Resource res) {
        synchronized (spendMutex){
            if (res == null){
                logger.error("неизвестный тип");
                return;
            }

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

                if (type == null){
                    logger.error("неизвестный тип ресурса");
                    return false;
                }

                int required = entry.getValue();
                int available = resources.get(type).getAmount();
                if (available < required) {
                    logger.debug("Не хватает ресурса {}: требуется {}, имеется {}", type, required, available);
                    return false;
                }
            }

            for (var entry : cost.entrySet()) {
                removeResource(entry.getKey(), entry.getValue());
            }
        }

        logger.info("Успешно потрачены ресурсы: {}", cost);
        return true;
    }

    @Override
    public void addResourceFromCost(Map<ResourceType, Integer> cost) {
        synchronized (spendMutex){
            for (var entry : cost.entrySet()){
                addResource(ResourcesBuilder.generate(entry.getKey(), entry.getValue()));
            }
        }
    }
}
