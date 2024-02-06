package cz.muni.fi.pv168.project.ui.filters.recipes;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ingredient;

import java.util.List;

public record RecipeFilterAttributes(
        String recipeName,
        List<Ingredient> ingredients,
        List<Category> category,
        Integer calMin,
        Integer calMax,
        Integer portionsMin,
        Integer portionsMax
) {
}
