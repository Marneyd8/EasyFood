package cz.muni.fi.pv168.project.ui.model;

import javax.swing.table.TableModel;
import java.util.Collection;

/**
 * The {@link EntityTableModel} interface provides an ability to get the actual entity at a certain index.
 *
 * @param <E> The entity type which the table keeps in a list.
 */
public interface EntityTableModel<E> extends TableModel {
    /**
     * Gets the entity at a certain index.
     *
     * @param rowIndex The index of the requested entity
     * @throws IndexOutOfBoundsException in case the rowIndex is less than zero or greater or equal
     *                                   than number of items in the table
     */
    E getEntity(int rowIndex);

    void deleteRow(int rowIndex);
    void deleteMultipleRows(Collection<Integer> rowIndices);

    void addRow(E entity);

    void updateRow(E entity);
}
