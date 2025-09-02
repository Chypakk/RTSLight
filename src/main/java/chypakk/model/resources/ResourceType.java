package chypakk.model.resources;

public enum ResourceType {
    GOLD("GOLD"), WOOD("WOOD");

    private final String type;

    ResourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ResourceType fromType(String type) {
        for (ResourceType resourceType : values()){
            if (resourceType.getType().equals(type)) return resourceType;
        }
        return null;
    }
}
