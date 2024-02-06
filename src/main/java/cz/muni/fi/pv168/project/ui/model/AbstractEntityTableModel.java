package cz.muni.fi.pv168.project.ui.model;

import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractEntityTableModel<T extends Entity> extends AbstractTableModel implements EntityTableModel<T> {

    public List<T> entities;
    private final List<Column<T, ?>> columns;
    public final CrudService<T> crudService;

    public AbstractEntityTableModel(List<Column<T, ?>> columns, List<T> entities, CrudService<T> crudService) {
        this.columns = columns;
        this.entities = new ArrayList<>(entities);
        this.crudService = crudService;
    }

    @Override
    public int getRowCount() {
        return entities.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T entity = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(entity);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        T entity = getEntity(rowIndex);
        columns.get(columnIndex).setValue(value, entity);
    }

    @Override
    public void deleteRow(int rowIndex) {
        T entity = getEntity(rowIndex);
        ValidationResult validationResult = crudService.deleteByGuid(entity.getGuid(), false);
        if (validationResult.isValid()) {
            entities.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    @Override
    public void deleteMultipleRows(Collection<Integer> rowIndices) {
        var guids = rowIndices.stream()
                .map(i -> getEntity(i).getGuid())
                .toList();
        ValidationResult validationResult = crudService.deleteMultipleByGuids(guids);

        if (validationResult.isValid()) {
            rowIndices.forEach(
                    i -> {
                        entities.remove(i);
                    }
            );
        }
        refresh();
    }

    public void addRow(T entity) {
        ValidationResult validationResult = crudService.create(entity);
        if (validationResult.isValid()) {
            entities.add(entity);
            refresh();
        } else {
            JOptionPane.showMessageDialog(
                    new JPanel(),
                    validationResult
            );
        }
    }

    public void updateRow(T entity) {
        ValidationResult validationResult = crudService.update(entity);
        if (validationResult.isValid()) {
            refresh();
        } else {
            JOptionPane.showMessageDialog(
                    new JPanel(),
                    String.join(", ", validationResult.getValidationErrors())
            );
        }
    }

    public T getEntity(int rowIndex) {
        return entities.get(rowIndex);
    }

    public List<T> getEntities() {
        return entities;
    }

    public void refresh() {
        this.entities = new ArrayList<>(crudService.findAll());
        fireTableDataChanged();
    }

}
