package cz.muni.fi.pv168.project.ui.filters;

import javax.swing.*;
import javax.swing.table.TableModel;

public class SearchBarRowFilter<T extends TableModel> extends RowFilter<T, Integer> {

    private final String filterName;
    private final boolean removeActive;

    public SearchBarRowFilter(String filterName, boolean removeActive) {
        this.filterName = filterName;
        this.removeActive = removeActive;
    }

    @Override
    public boolean include(Entry<? extends T, ? extends Integer> entry) {
        if (removeActive) {
            return true;
        }
        T model = entry.getModel();
        String name = (String) model.getValueAt(entry.getIdentifier(), 0);
        return name.startsWith(filterName);
    }
}
