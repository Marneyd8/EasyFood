package cz.muni.fi.pv168.project.ui.action;

import cz.muni.fi.pv168.project.wiring.CommonDependencyProvider;

import javax.swing.*;

public class ActionFactory {
    private final AddAction addAction;
    private final DeleteAction deleteAction;
    private final EditAction editAction;
    private final QuitAction quitAction;
    private final ShowAction showAction;
    private final CommonDependencyProvider commonDependencyProvider;

    public ActionFactory(
            JTable recipeTable,
            JTable ingredientsTable,
            JTable unitsTable,
            JTable categoryTable,
            CommonDependencyProvider commonDependencyProvider
    ) {
        this.commonDependencyProvider = commonDependencyProvider;
        this.addAction = new AddAction(recipeTable, ingredientsTable, unitsTable, categoryTable, commonDependencyProvider);
        this.deleteAction = new DeleteAction(recipeTable, ingredientsTable, unitsTable, categoryTable);
        this.editAction = new EditAction(recipeTable, ingredientsTable, unitsTable, categoryTable, commonDependencyProvider);
        this.showAction = new ShowAction(recipeTable, ingredientsTable, unitsTable, categoryTable);
        this.quitAction = new QuitAction();
    }

    public AddAction getAddAction() {
        return addAction;
    }

    public DeleteAction getDeleteAction() {
        return deleteAction;
    }

    public EditAction getEditAction() {
        return editAction;
    }

    public QuitAction getQuitAction() {
        return quitAction;
    }

    public ShowAction getShowAction() {
        return showAction;
    }
}
