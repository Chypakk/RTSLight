package chypakk.model.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourcesBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesBuilder.class);

    public static Resource generate(ResourceType type, int amount){
        return switch (type){
            case WOOD -> new Wood(amount);
            case GOLD -> new Gold(amount);
            case null -> {
                logger.error("неизвестный тип");
                yield null;
            }
        };
    }
}
