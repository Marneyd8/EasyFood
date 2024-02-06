package cz.muni.fi.pv168.project.ui.filters.ingredients;

import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;

import javax.swing.*;

public class IngredientRowFilter extends RowFilter<IngredientTableModel, Integer> {

    private final IngredientMatcher matcher;

    public IngredientRowFilter(IngredientFilterAttributes attributes) {
        this.matcher = new IngredientMatcher(attributes);
    }

    @Override
    public boolean include(Entry<? extends IngredientTableModel, ? extends Integer> entry) {
        IngredientTableModel tableModel = entry.getModel();

        return matcher.evaluate(tableModel.getEntity(entry.getIdentifier()));
    }
}
