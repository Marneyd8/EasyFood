package cz.muni.fi.pv168.project.ui.filters.ingredients;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.ui.filters.EntityMatcher;

public class IngredientMatcher extends EntityMatcher<Ingredient> {

    private final IngredientFilterAttributes attributes;

    public IngredientMatcher(IngredientFilterAttributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean evaluate(Ingredient ingredient) {
        return attributes.minCalories() <= ingredient.getNutritionalValue()
                && attributes.maxCalories() >= ingredient.getNutritionalValue();
    }
}
