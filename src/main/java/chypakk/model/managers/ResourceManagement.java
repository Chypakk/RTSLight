package chypakk.model.managers;

import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;

import java.util.Map;

public interface ResourceManagement {
    void addResource(Resource res);
    int getResource(ResourceType type);
    void removeResource(ResourceType type, int amount);
    void printResources();
    boolean trySpendResources(Map<ResourceType, Integer> cost);
}
