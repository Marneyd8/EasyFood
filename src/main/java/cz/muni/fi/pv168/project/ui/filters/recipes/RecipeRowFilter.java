package cz.muni.fi.pv168.project.ui.filters.recipes;

import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;

public class RecipeRowFilter extends RowFilter<RecipeTableModel, Integer> {

    private final RecipeMatcher matcher;
    private final boolean removeActive;

    public RecipeRowFilter(RecipeFilterAttributes recipeFilterAttributes, boolean removeActive) {
        this.matcher = new RecipeMatcher(recipeFilterAttributes);
        this.removeActive = removeActive;
    }

    @Override
    public boolean include(Entry<? extends RecipeTableModel, ? extends Integer> entry) {
        if (removeActive) {
            return true;
        }

        RecipeTableModel tableModel = entry.getModel();

        return matcher.evaluate(tableModel.getEntity(entry.getIdentifier()));
    }
}
