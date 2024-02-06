package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.listeners.StatisticsUpdater;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.ActionEvent;

public class RemoveRecipesFilterAction<T extends TableModel> extends AbstractAction {

    private final TableRowSorter<T> recipeTableRowSorter;

    public RemoveRecipesFilterAction(TableRowSorter<T> recipeTableRowSorter) {
        super("", Icons.DELETE_ICON);
        this.recipeTableRowSorter = recipeTableRowSorter;

        putValue(SHORT_DESCRIPTION, "Remove filter");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        recipeTableRowSorter.setRowFilter(new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends T, ? extends Integer> entry) {
                return true;
            }
        });
        StatisticsUpdater.reload();
    }
}
