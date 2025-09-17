package chypakk.model.managers;

import chypakk.model.resources.Resource;
import chypakk.model.resources.ResourceType;

public interface ResourceManagement {
    void addResource(Resource res);
    int getResource(ResourceType type);
    void removeResource(ResourceType type, int amount);
    void printResources();
}
