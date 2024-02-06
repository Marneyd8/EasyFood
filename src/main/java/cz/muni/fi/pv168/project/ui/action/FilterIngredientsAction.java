package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.filters.ingredients.IngredientFilterAttributes;
import cz.muni.fi.pv168.project.ui.filters.ingredients.IngredientRowFilter;
import cz.muni.fi.pv168.project.ui.listeners.StatisticsUpdater;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;

public class FilterIngredientsAction extends AbstractAction {

    public static final String FILTER_TAG = "Filter";
    public static final String REMOVE_FILTER_TAG = "Remove filter";

    private final TableRowSorter<IngredientTableModel> ingredientTableSorter;
    private final JSpinner caloriesMinFilter;
    private final JSpinner caloriesMaxFilter;
    private boolean isFilterApplied = false;
    private final Icon defaultIcon = Icons.FILTER_ICON;
    private final Icon pressedIcon = Icons.PRESSED_ICON;

    public FilterIngredientsAction(
            TableRowSorter<IngredientTableModel> ingredientTableSorter,
            JSpinner caloriesMinFilter,
            JSpinner caloriesMaxFilter
    ) {
        super("", Icons.FILTER_ICON);
        this.ingredientTableSorter = ingredientTableSorter;
        this.caloriesMinFilter = caloriesMinFilter;
        this.caloriesMaxFilter = caloriesMaxFilter;

        putValue(Action.SHORT_DESCRIPTION, FILTER_TAG);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isFilterApplied) {
            IngredientFilterAttributes attributes = new IngredientFilterAttributes(
                    (Integer) caloriesMinFilter.getValue(),
                    (Integer) caloriesMaxFilter.getValue()
            );

            ingredientTableSorter.setRowFilter(new IngredientRowFilter(attributes));
            isFilterApplied = true;
            putValue(Action.SMALL_ICON, pressedIcon);
            putValue(Action.SHORT_DESCRIPTION, REMOVE_FILTER_TAG);
        } else {
            ingredientTableSorter.setRowFilter(null);
            isFilterApplied = false;
            putValue(Action.SMALL_ICON, defaultIcon);
            putValue(Action.SHORT_DESCRIPTION, FILTER_TAG);
        }
        StatisticsUpdater.reload();
    }
}
