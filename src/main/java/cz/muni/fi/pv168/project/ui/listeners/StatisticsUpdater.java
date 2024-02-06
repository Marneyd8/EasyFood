package cz.muni.fi.pv168.project.ui.listeners;

import cz.muni.fi.pv168.project.ui.action.TabbedPanelContext;

import javax.swing.*;

public class StatisticsUpdater {
    private static JLabel statistics;

    public static void reload() {
        String text;
        switch (TabbedPanelContext.getActiveTab()) {
            case 0:
                text = "Showing recipes ";
                break;
            case 1:
                text = "Showing ingredients ";
                break;
            case 2:
                text = "Showing units ";
                break;
            default:
                text = "Showing categories ";
        }
        JTable table = TabbedPanelContext.getActiveTable();
        StringBuilder builder = new StringBuilder()
                .append(text)
                .append(table.getRowCount())
                .append(" out of ")
                .append(table.getModel().getRowCount());
        statistics.setText(builder.toString());
    }

    public static void setLabel(JLabel label) {
        statistics = label;
    }
}
