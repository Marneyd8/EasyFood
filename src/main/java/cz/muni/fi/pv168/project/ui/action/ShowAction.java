package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.ui.dialog.ShowDialog;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ShowAction extends ContextAction {
    public ShowAction(JTable recipeTable, JTable ingredientTable, JTable unitsTable, JTable categoryTable) {
        super(recipeTable, ingredientTable, unitsTable, categoryTable, "", Icons.SHOW_ICON);
        putValue(SHORT_DESCRIPTION, "Shows recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable activeTable = TabbedPanelContext.getActiveTable();
        int[] selectedRows = activeTable.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + selectedRows.length);
        }
        if (activeTable.isEditing()) {
            activeTable.getCellEditor().cancelCellEditing();
        }
        RecipeTableModel recipeTableModel = (RecipeTableModel) activeTable.getModel();
        int modelRow = activeTable.convertRowIndexToModel(selectedRows[0]);
        Recipe recipe = recipeTableModel.getEntity(modelRow);
        ShowDialog showDialog = new ShowDialog(recipe);
        showDialog.getRecipeInstruction();
    }
}
