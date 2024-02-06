package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.BaseUnit;
import cz.muni.fi.pv168.project.business.model.CustomUnit;
import cz.muni.fi.pv168.project.ui.model.CustomUnitTableModel;

import javax.swing.*;

public class CustomUnitDialog extends EntityDialog<CustomUnit> {


    private final JTextField customUnitNameField = new JTextField();
    private final JTextField customUnitAbbreviationField = new JTextField();
    private final JSpinner customUnitAmount = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 50000.0, 0.01));
    private final JComboBox<BaseUnit> units = new JComboBox<>(BaseUnit.values());

    public CustomUnitDialog(CustomUnit unit, CustomUnitTableModel unitTableModel) {
        super(unit, unitTableModel.getEntities());

        if (unit != null) {
            setValues();
        } else {
            entity = new CustomUnit(null, null, 0, null);
        }
        addFields();
    }

    private void setValues() {
        customUnitNameField.setText(entity.getName());
        customUnitAbbreviationField.setText(entity.getAbbreviation());
        customUnitAmount.setValue(Double.parseDouble(entity.getBaseAmountNumber()));
        units.setSelectedIndex(entity.getBaseUnit().getIndex());
    }

    private void addFields() {
        add("Name:", customUnitNameField);
        add("Abbreviation", customUnitAbbreviationField);
        add("Base Amount", customUnitAmount);
        add("Base Unit", units);
    }

    @Override
    CustomUnit getEntity() {
        CustomUnit setEntity = new CustomUnit();
        setEntity.setGuid(entity.getGuid());
        setEntity.setName(customUnitNameField.getText());
        setEntity.setAbbreviation(customUnitAbbreviationField.getText());
        setEntity.setAmount((double) customUnitAmount.getValue());
        setEntity.setBaseUnit((BaseUnit) units.getSelectedItem());
        return setEntity;
    }
}
