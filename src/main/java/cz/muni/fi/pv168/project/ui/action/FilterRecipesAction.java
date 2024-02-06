package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.ui.filters.ingredients.IngredientFilterAttributes;
import cz.muni.fi.pv168.project.ui.filters.ingredients.IngredientRowFilter;
import cz.muni.fi.pv168.project.ui.filters.recipes.RecipeFilterAttributes;
import cz.muni.fi.pv168.project.ui.filters.recipes.RecipeRowFilter;
import cz.muni.fi.pv168.project.ui.listeners.StatisticsUpdater;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;
import cz.muni.fi.pv168.project.ui.specialComponents.MultiSelectCombobox;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;

public class FilterRecipesAction extends AbstractAction {

    public static final String FILTER_TAG = "Filter";
    public static final String REMOVE_FILTER_TAG = "Remove filter";
    private final TableRowSorter<RecipeTableModel> recipeTableRowSorter;
    private final MultiSelectCombobox<Ingredient> ingredientFilter;
    private final MultiSelectCombobox<Category> categoryFilter;
    private final JSpinner caloriesMinFilter;
    private final JSpinner caloriesMaxFilter;
    private final JSpinner portionsMinFilter;
    private final JSpinner portionsMaxFilter;
    private final JTable recipeTable;
    private boolean isFilterApplied = false;
    private final Icon defaultIcon = Icons.FILTER_ICON;
    private final Icon pressedIcon = Icons.PRESSED_ICON;

    public FilterRecipesAction(
            MultiSelectCombobox<Ingredient> ingredientFilter,
            MultiSelectCombobox<Category> categoryFilter,
            JSpinner caloriesMinFilter,
            JSpinner caloriesMaxFilter,
            JSpinner portionsMinFilter,
            JSpinner portionsMaxFilter,
            JTable recipeTable,
            TableRowSorter<RecipeTableModel> recipeTableRowSorter
    ) {
        super("", Icons.FILTER_ICON);
        this.ingredientFilter = ingredientFilter;
        this.categoryFilter = categoryFilter;
        this.caloriesMinFilter = caloriesMinFilter;
        this.caloriesMaxFilter = caloriesMaxFilter;
        this.portionsMinFilter = portionsMinFilter;
        this.portionsMaxFilter = portionsMaxFilter;
        this.recipeTable = recipeTable;
        this.recipeTableRowSorter = recipeTableRowSorter;

        putValue(Action.SHORT_DESCRIPTION, FILTER_TAG);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isFilterApplied) {
            RecipeFilterAttributes attributes = new RecipeFilterAttributes(
                    null,
                    ingredientFilter.getSelectedItems(),
                    categoryFilter.getSelectedItems(),
                    (Integer) caloriesMinFilter.getValue(),
                    (Integer) caloriesMaxFilter.getValue(),
                    (Integer) portionsMinFilter.getValue(),
                    (Integer) portionsMaxFilter.getValue()
            );

            recipeTableRowSorter.setRowFilter(new RecipeRowFilter(attributes, false));
            isFilterApplied = true;
            putValue(Action.SMALL_ICON, pressedIcon);
            putValue(Action.SHORT_DESCRIPTION, REMOVE_FILTER_TAG);
        } else {
            recipeTableRowSorter.setRowFilter(null);
            isFilterApplied = false;
            putValue(Action.SMALL_ICON, defaultIcon);
            putValue(Action.SHORT_DESCRIPTION, FILTER_TAG);
        }
        StatisticsUpdater.reload();
    }
}
