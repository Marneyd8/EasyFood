package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.ui.model.CategoryTableModel;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class CategoryDialog extends EntityDialog<Category> {

    private final JTextField categoryNameField = new JTextField();
    private final JColorChooser categoryColor = new JColorChooser();

    public CategoryDialog(Category category, CategoryTableModel categoryTableModel) {
        super(category, categoryTableModel.getEntities());

        for (AbstractColorChooserPanel panel : categoryColor.getChooserPanels()) {
            if (!panel.getDisplayName().equals("RGB")) {
                categoryColor.removeChooserPanel(panel);
            }
        }
        categoryColor.setPreviewPanel(new JPanel());

        if (category != null) {
            setValues();
        } else {
            entity = new Category(null, null);
        }
        addFields();
    }

    private void setValues() {
        categoryNameField.setText(entity.getName());
        categoryColor.setColor(entity.getColor());
    }

    private void addFields() {
        add("Name:", categoryNameField);
        add("Color", categoryColor);
    }

    @Override
    Category getEntity() {
        Category setEntity = new Category();
        setEntity.setGuid(entity.getGuid());
        setEntity.setName(categoryNameField.getText());
        setEntity.setColor(categoryColor.getColor());
        return setEntity;
    }
}
