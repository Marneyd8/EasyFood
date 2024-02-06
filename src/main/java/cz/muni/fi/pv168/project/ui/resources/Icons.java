package cz.muni.fi.pv168.project.ui.resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;

public final class Icons {

    public static final Icon DELETE_ICON = createIcon("Crystal_Clear_action_button_cancel.png");
    public static final Icon EDIT_ICON = createIcon("Crystal_Clear_action_edit.png");
    public static final Icon ADD_ICON = createIcon("Crystal_Clear_action_edit_add.png");
    public static final Icon QUIT_ICON = createIcon("Crystal_Clear_action_exit.png");
    public static final Icon FILTER_ICON = createIcon("Filter_Deactive.png");
    public static final Icon PRESSED_ICON = createIcon("Filter_Active.png");
    public static final Icon SHOW_ICON = createIcon("Spy_Eye.png");
    public static final Icon SELECTED_ICON = createIcon("SELECTED_ICON.png");


    private Icons() {
        throw new AssertionError("This class is not instantiable");
    }

    private static ImageIcon createIcon(String name) {
        URL url = Icons.class.getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Icon resource not found on classpath: " + name);
        }
        return new ImageIcon(url);
    }
}
