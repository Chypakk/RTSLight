package chypakk.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record UnitConfig(
        @JsonProperty("type") String type,
        @JsonProperty("label") String label,
        @JsonProperty("health") int health,
        @JsonProperty("baseDamage") int baseDamage,
        @JsonProperty("attackStrategy") String attackStrategy,
        @JsonProperty("cost") Map<String, Integer> cost
) {}
