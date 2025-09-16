package chypakk.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record GeneratorConfig(
        @JsonProperty("type") String type,
        @JsonProperty("label") String label,
        @JsonProperty("order") int order,
        @JsonProperty("interval") int interval,
        @JsonProperty("amountPerInterval") int amountPerInterval,
        @JsonProperty("totalAmount") int totalAmount,
        @JsonProperty("cost") Map<String, Integer> cost
) {}