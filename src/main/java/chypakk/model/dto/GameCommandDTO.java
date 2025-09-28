package chypakk.model.dto;

public interface GameCommandDTO {
    record AddBuilding(String buildingType) implements GameCommandDTO {}
    record AddGenerator(String generatorType) implements GameCommandDTO {}
    record RecruitUnit(String unitType) implements GameCommandDTO {}
    record ExchangeResource(String fromType, int fromAmount, String toType, int toAmount) implements GameCommandDTO {}
    record ShowResources() implements GameCommandDTO {}
    record ShowBuildings() implements GameCommandDTO {}
    record ShowGenerators() implements GameCommandDTO {}
    record ExitGame() implements GameCommandDTO {}
}
