package chypakk.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameConfig(
        @JsonProperty("resources") List<ResourceConfig> resources,
        @JsonProperty("generators") List<GeneratorConfig> generators,
        @JsonProperty("buildings") List<BuildingConfig> buildings,
        @JsonProperty("units") List<UnitConfig> units,
        @JsonProperty("exchanges") List<ExchangeConfig> exchanges
) {}
