package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.BaseUnit;
import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;

/**
 * @author Adam Juhas
 */
public class UnitMapper implements EntityMapper<UnitEntity, CustomUnit> {

    //private final BaseUnitDao baseUnitDao;
    //private final EntityMapper<BaseUnitEntity, BaseUnit> baseUnitMapper;

    public UnitMapper(
    ) {
        //this.baseUnitDao = baseUnitDao;
        //this.baseUnitMapper = baseUnitMapper;
    }

    @Override
    public CustomUnit mapToBusiness(UnitEntity dbCustomUnit) {
        /*var baseUnit = baseUnitDao
                .findById(dbCustomUnit.baseUnitId())
                .map(baseUnitMapper::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("BaseUnit not found, id: " +
                        dbCustomUnit.baseUnitId()));*/
        return new CustomUnit(
                dbCustomUnit.guid(),
                dbCustomUnit.unitName(),
                dbCustomUnit.abbreviation(),
                dbCustomUnit.amount(),
                BaseUnit.indexToUnit(dbCustomUnit.baseUnitId())
        );
    }

    @Override
    public UnitEntity mapNewEntityToDatabase(CustomUnit entity) {
        return getUnitEntity(entity, null);
    }

    @Override
    public UnitEntity mapExistingEntityToDatabase(CustomUnit entity, Long dbId) {
        return getUnitEntity(entity, dbId);
    }

    private UnitEntity getUnitEntity(CustomUnit entity, Long dbId) {
        /*var baseUnitEntity = baseUnitDao
                .findByGuid(entity.getBaseUnit().getGuid())
                .orElseThrow(() -> new DataStorageException("BaseUnit not found, guid: " +
                        entity.getBaseUnit().getGuid()));*/
        return new UnitEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                entity.getAbbreviation(),
                entity.getAmount(),
                entity.getBaseUnit().getIndex()
        );
    }
}
