package chypakk.model.managers;

import chypakk.config.ResourceConfig;
import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager implements ResourceManagement {
    private final Map<ResourceType, Resource> resources = new ConcurrentHashMap<>();

    public ResourceManager(List<ResourceConfig> resources) {
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
        Resource existing = resources.get(res.getType());
        if (existing != null) {
            existing.addAmount(res.getAmount());
        } else {
            resources.put(res.getType(), res);
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
        resources.get(type).removeAmount(amount);
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
    public boolean tryRemoveResource(Map<ResourceType, Integer> cost) {
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

        return true;
    }
}
