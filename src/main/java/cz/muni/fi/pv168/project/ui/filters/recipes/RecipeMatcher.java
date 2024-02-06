package cz.muni.fi.pv168.project.ui.filters.recipes;

import cz.muni.fi.pv168.project.business.model.AddedIngredient;
import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.ui.filters.EntityMatcher;

import java.util.Optional;

public class RecipeMatcher extends EntityMatcher<Recipe> {
    private final RecipeFilterAttributes recipeFilterAttributes;

    public RecipeMatcher(RecipeFilterAttributes recipeFilterAttributes) {
        this.recipeFilterAttributes = recipeFilterAttributes;
    }

    @Override
    public boolean evaluate(Recipe recipe) {
        if (recipeFilterAttributes.recipeName() != null) {
            return recipe.getName().startsWith(recipeFilterAttributes.recipeName());
        }
        return ingredientMatch(recipe)
                && categoryMatch(recipe)
                && caloriesRangeMatch(recipe)
                && portionsRangeMatch(recipe);
    }

    private boolean ingredientMatch(Recipe recipe) {
        if (recipeFilterAttributes == null || recipeFilterAttributes.ingredients().isEmpty()) {
            return true;
        }
        for (Ingredient ingredient : recipeFilterAttributes.ingredients()) {
            Optional<AddedIngredient> addedIngredient = recipe.getAddedIngredients().stream()
                    .filter(ai -> ai.getIngredient().getGuid().equals(ingredient.getGuid()))
                    .findFirst();
            if (addedIngredient.isPresent()) {
                return true;
            }
        }
        return false;
    }

    private boolean categoryMatch(Recipe recipe) {
        if (recipeFilterAttributes.category().isEmpty()) {
            return true;
        }
        if (recipe.getCategory() == null) {
            return false;
        }
        for (Category category : recipeFilterAttributes.category()) {
            if (recipe.getCategory().getGuid().equals(category.getGuid())) {
                return true;
            }
        }
        return false;
    }

    private boolean caloriesRangeMatch(Recipe recipe) {
        if (recipeFilterAttributes.calMin() == null) {
            return true;
        }
        return recipeFilterAttributes.calMin() <= recipe.getRecipeNutritionalValue()
                && recipeFilterAttributes.calMax() >= recipe.getRecipeNutritionalValue();
    }

    private boolean portionsRangeMatch(Recipe recipe) {
        if (recipeFilterAttributes.portionsMin() == null) {
            return true;
        }
        return recipeFilterAttributes.portionsMin() <= recipe.getPortions()
                && recipeFilterAttributes.portionsMax() >= recipe.getPortions();
    }
}
