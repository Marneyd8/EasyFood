package cz.muni.fi.pv168.project.business.model;

import java.text.DecimalFormat;

public class AddedIngredient extends Entity {

    private Ingredient ingredient;
    private Double quantity;
    private Unit unit;
    private Recipe recipe;
    private final DecimalFormat amountFormat = new DecimalFormat("#.##");

    public AddedIngredient() {
    }

    public AddedIngredient(Ingredient ingredient, Double quantity, Unit unit) {
        this.ingredient = ingredient;
        this.quantity = quantity;
        this.unit = unit;
    }

    public AddedIngredient(
            String guid,
            Ingredient ingredient,
            Recipe recipe,
            Unit unit,
            Double quantity
    ) {
        super(guid);
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.unit = unit;
        this.quantity = quantity;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

}
