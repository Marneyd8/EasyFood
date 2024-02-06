package cz.muni.fi.pv168.project.storage.sql.entity;

public record IngredientEntity(
        Long id,
        String guid,
        String ingredientName,
        int nutritionalValue
) {
    public IngredientEntity(Long id, String guid, String ingredientName, int nutritionalValue) {
        this.id = id;
        this.guid = guid;
        this.ingredientName = ingredientName;
        this.nutritionalValue = nutritionalValue;
    }

    public IngredientEntity(String guid, String ingredientName, int nutritionalValue) {
        this(null, guid, ingredientName, nutritionalValue);
    }
}
