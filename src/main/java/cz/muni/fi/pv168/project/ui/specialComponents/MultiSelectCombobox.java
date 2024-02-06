package cz.muni.fi.pv168.project.ui.specialComponents;

import cz.muni.fi.pv168.project.ui.resources.Icons;

import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Filip Skvara
 */
public class MultiSelectCombobox<T> extends JButton {
    private List<T> items;
    private List<T> selectedItems = new ArrayList<>();
    private JScrollPopupMenu menu;

    private boolean showing = false;

    public MultiSelectCombobox(List<T> items, String name) {
        super(name);
        JComboBox<T> coloring = new JComboBox<>();
        super.setBorder(coloring.getBorder());
        super.setBackground(coloring.getBackground());
        this.items = items;
        createMenu();
        addActionListener(e -> {
            if (!showing) {
                menu.show(this, 0, this.getHeight());
                showing = true;
                return;
            }
            showing = false;

        });
    }

    private JPopupMenu createMenu() {
        menu = new JScrollPopupMenu();
        menu.setMaximumVisibleRows(6);
        for (T item : items) {
            menu.add(new SelectedAction<>(selectedItems, item, menu, this));
        }
        return menu;
    }

    public void reload(List<T> items) {
        this.items = items;
        this.selectedItems = new ArrayList<>();
        createMenu();
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }
}

class SelectedAction<T> extends AbstractAction {

    public List<T> selectedItems;
    private final T item;
    private final JScrollPopupMenu menu;
    private final JButton button;

    private boolean selected = false;

    public SelectedAction(List<T> selectedItems, T item, JScrollPopupMenu menu, MultiSelectCombobox<T> button) {
        super(item.toString());
        this.item = item;
        this.menu = menu;
        this.button = button;
        this.selectedItems = selectedItems;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int value = menu.getScrollBar().getValue();
        menu.show(button, 0, button.getHeight());
        menu.getScrollBar().setValue(value);
        if (selected) {
            selectedItems.remove(item);
            putValue(Action.SMALL_ICON, null);
            selected = false;
        } else {
            selectedItems.add(item);
            putValue(Action.SMALL_ICON, Icons.SELECTED_ICON);
            selected = true;
        }
    }
}
