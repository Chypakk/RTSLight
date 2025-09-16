package chypakk.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResourceConfig(@JsonProperty("type") String type,
                             @JsonProperty("label") String label,
                             @JsonProperty("order") int order,
                             @JsonProperty("initialAmount") int initialAmount) {
}
