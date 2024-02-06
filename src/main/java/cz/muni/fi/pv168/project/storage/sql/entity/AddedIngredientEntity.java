package cz.muni.fi.pv168.project.storage.sql.entity;

public record AddedIngredientEntity(
        Long id,
        String guid,
        Long ingredientId,
        Long recipeId,
        Long unitId,
        Double quantity,
        Integer baseUnit) {

    public AddedIngredientEntity(
            Long id,
            String guid,
            Long ingredientId,
            Long recipeId,
            Long unitId,
            Double quantity,
            Integer baseUnit
    ) {
        this.id = id;
        this.recipeId = recipeId;
        this.guid = guid;
        this.ingredientId = ingredientId;
        this.unitId = unitId;
        this.quantity = quantity;
        this.baseUnit= baseUnit;
    }
}
