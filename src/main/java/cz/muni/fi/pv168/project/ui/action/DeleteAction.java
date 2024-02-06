package cz.muni.fi.pv168.project.ui.action;


import cz.muni.fi.pv168.project.ui.listeners.StatisticsUpdater;
import cz.muni.fi.pv168.project.ui.model.AbstractEntityTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class DeleteAction extends ContextAction {


    public DeleteAction(JTable recipeTable, JTable ingredientTable, JTable unitsTable, JTable categoryTable) {
        super(recipeTable, ingredientTable, unitsTable, categoryTable, "", Icons.DELETE_ICON);

        putValue(SHORT_DESCRIPTION, "Deletes selected items");
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JTable activeTable = TabbedPanelContext.getActiveTable();
        //System.out.println(activeTable.getRowCount());
        AbstractEntityTableModel tableModel = (AbstractEntityTableModel) activeTable.getModel();
        List<Integer> indexes = Arrays.stream(activeTable.getSelectedRows())
                // view row index must be converted to model row index
                .map(activeTable::convertRowIndexToModel)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .toList();

        // empty indexes should not exist, due to delete button inactivity during zero selected rows
        if (indexes.size() > 1) {
            tableModel.deleteMultipleRows(indexes);
        } else {
            tableModel.deleteRow(indexes.get(0));
        }
        StatisticsUpdater.reload();
    }
}
