package cz.muni.fi.pv168.project.storage.sql.entity;

public record UnitEntity(
        Long id,
        String guid,
        String unitName,
        String abbreviation,
        double amount,
        int baseUnitId // base units name
) {

    public UnitEntity(Long id, String guid, String unitName, String abbreviation, double amount, int baseUnitId) {
        this.id = id;
        this.guid = guid;
        this.unitName = unitName;
        this.abbreviation = abbreviation;
        this.amount = amount;
        this.baseUnitId = baseUnitId;
    }

    public UnitEntity(String guid, String unitName, String abbreviation, double amount, int baseUnitId) {
        this(null, guid, unitName, abbreviation, amount, baseUnitId);
    }
}

