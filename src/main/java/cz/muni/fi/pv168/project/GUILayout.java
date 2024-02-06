package cz.muni.fi.pv168.project;

import javax.swing.*;

public class GUILayout {
    private JPanel mainPanel;
    private JTextField searchRecipesTextField;
    private JTabbedPane tabbedPanels;
    private JComboBox comboBoxFilters;
    private JComboBox comboBoxCategories;
    private JList recipeList;
    private JList ingredientList;
    private JList unitList;
    private JList categoryList;
    private JToolBar toolBarTop;
    private JPanel sidePanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton showRecipeButton;
    private JButton filterButton;

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getSearchRecipesTextField() {
        return searchRecipesTextField;
    }

    public JList getRecipeList() {
        return recipeList;
    }

    public JList getIngerdientList() {
        return ingredientList;
    }

    public JList getUnitList() {
        return unitList;
    }

    public JButton getShowRecipeButton() {
        return showRecipeButton;
    }

    public JButton getFilterButton() {
        return filterButton;
    }

    public JList getCategoryList() {
        return categoryList;
    }

    public JTabbedPane getTabbedPanels() {
        return tabbedPanels;
    }

    public JComboBox getComboBoxFilters() {
        return comboBoxFilters;
    }

    public JComboBox getComboBoxCategories() {
        return comboBoxCategories;
    }

    public JToolBar getToolBarTop() {
        return toolBarTop;
    }

    public JPanel getSidePanel() {
        return sidePanel;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

}

