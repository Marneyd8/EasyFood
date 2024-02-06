package cz.muni.fi.pv168.project.ui.action;

import javax.swing.*;

abstract public class ContextAction extends AbstractAction {
    protected final JTable recipeTable;
    protected final JTable ingredientTable;
    protected final JTable unitsTable;
    protected final JTable categoryTable;

    public ContextAction(JTable recipeTable, JTable ingredientTable, JTable unitsTable, JTable categoryTable, String name, Icon icon) {
        super(name, icon);
        this.recipeTable = recipeTable;
        this.ingredientTable = ingredientTable;
        this.unitsTable = unitsTable;
        this.categoryTable = categoryTable;
    }

}
