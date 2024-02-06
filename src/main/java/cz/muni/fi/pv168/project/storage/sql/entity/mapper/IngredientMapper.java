package cz.muni.fi.pv168.project.storage.sql.entity.mapper;


import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;

public class IngredientMapper implements EntityMapper<IngredientEntity, Ingredient> {

    private final DataAccessObject<UnitEntity> unitDao;
    private final EntityMapper<UnitEntity, CustomUnit> unitMapper;

    public IngredientMapper(
            DataAccessObject<UnitEntity> unitDao,
            EntityMapper<UnitEntity, CustomUnit> unitMapper
    ) {
        this.unitDao = unitDao;
        this.unitMapper = unitMapper;
    }

    @Override
    public Ingredient mapToBusiness(IngredientEntity dbIngredient) {
        return new Ingredient(
                dbIngredient.guid(),
                dbIngredient.ingredientName(),
                dbIngredient.nutritionalValue()
        );
    }

    @Override
    public IngredientEntity mapNewEntityToDatabase(Ingredient entity) {
        return getIngredientEntity(entity, null);
    }

    @Override
    public IngredientEntity mapExistingEntityToDatabase(Ingredient entity, Long dbId) {
        return getIngredientEntity(entity, dbId);
    }

    private IngredientEntity getIngredientEntity(Ingredient entity, Long dbId) {
        return new IngredientEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                entity.getNutritionalValue()
        );
    }
}
