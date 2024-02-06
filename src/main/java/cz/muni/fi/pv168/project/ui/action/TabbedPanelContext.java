package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;
import cz.muni.fi.pv168.project.ui.model.CustomUnitTableModel;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;

import javax.swing.*;

public class TabbedPanelContext {
    private static int activeTab = 0;
    private static JTable recipeTable;
    private static JTable ingredientTable;
    protected static JTable unitsTable;
    protected static JTable categoryTable;


    public static void setTables(JTable rt, JTable it, JTable ut, JTable ct) {
        recipeTable = rt;
        ingredientTable = it;
        unitsTable = ut;
        categoryTable = ct;
    }

    public static void setActiveTab(int i) {
        activeTab = i;
        refreshChangedTable();
    }

    public static JTable getActiveTable() {
        return switch (activeTab) {
            case 0 -> recipeTable;
            case 1 -> ingredientTable;
            case 2 -> unitsTable;
            default -> categoryTable;
        };
    }

    public static void refreshChangedTable() {
        switch (activeTab) {
            case 0:
                RecipeTableModel recipeTableModel = (RecipeTableModel) recipeTable.getModel();
                recipeTableModel.refresh();
            case 1:
                IngredientTableModel ingredientTableModel = (IngredientTableModel) ingredientTable.getModel();
                ingredientTableModel.refresh();
            case 2:
                CustomUnitTableModel customUnitTableModel = (CustomUnitTableModel) unitsTable.getModel();
                customUnitTableModel.refresh();
            default:
                CategoryTableModel categoryTableModel = (CategoryTableModel) categoryTable.getModel();
                categoryTableModel.refresh();
        }
    }

    public static int getActiveTab() {
        return activeTab;
    }

}
