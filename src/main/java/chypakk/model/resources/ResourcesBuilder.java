package chypakk.model.resources;

public class ResourcesBuilder {

    public static Resource generate(ResourceType type, int amount){
        return switch (type){
            case WOOD -> new Wood(amount);
            case GOLD -> new Gold(amount);
        };
    }
}
