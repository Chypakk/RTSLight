package chypakk.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExchangeConfig(
        @JsonProperty("fromType") String fromType,
        @JsonProperty("fromAmount") int fromAmount,
        @JsonProperty("toType") String toType,
        @JsonProperty("toAmount") int toAmount
) {}