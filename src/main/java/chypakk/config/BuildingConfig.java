package chypakk.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record BuildingConfig(
        @JsonProperty("type") String type,
        @JsonProperty("label") String label,
        @JsonProperty("health") int health,
        @JsonProperty("cost") Map<String, Integer> cost
) {}
