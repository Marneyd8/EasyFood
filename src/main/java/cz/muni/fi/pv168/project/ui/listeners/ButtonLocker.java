package cz.muni.fi.pv168.project.ui.listeners;

import cz.muni.fi.pv168.project.ui.action.ActionFactory;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ButtonLocker implements ListSelectionListener {

    ActionFactory buttons;
    JTable table;

    public ButtonLocker(ActionFactory buttons, JTable table) {
        this.buttons = buttons;
        this.table = table;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        reload(buttons, table);
    }

    public static void reload(ActionFactory buttons, JTable table) {
        int selected = table.getSelectedRowCount();
        buttons.getDeleteAction().setEnabled(selected > 0);
        buttons.getEditAction().setEnabled(selected == 1);
        buttons.getShowAction().setEnabled(selected == 1 && table.getModel().getClass().equals(RecipeTableModel.class));
    }
}