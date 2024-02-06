package cz.muni.fi.pv168.project.ui.listeners;

import cz.muni.fi.pv168.project.ui.filters.SearchBarRowFilter;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SearchBarListener<TABLE_MODEL extends TableModel> extends KeyAdapter {
    private final JTextField searchBar;
    private final TableRowSorter<TABLE_MODEL> tableRowSorter;

    public SearchBarListener(
            JTextField searchBar,
            TableRowSorter<TABLE_MODEL> tableRowSorter
    ) {
        this.searchBar = searchBar;
        this.tableRowSorter = tableRowSorter;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        tableRowSorter.setRowFilter(new SearchBarRowFilter<>(
                searchBar.getText(),
                searchBar.getText().isEmpty()
        ));
        StatisticsUpdater.reload();
    }
}
