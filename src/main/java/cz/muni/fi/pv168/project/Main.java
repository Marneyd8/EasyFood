package cz.muni.fi.pv168.project;


import cz.muni.fi.pv168.project.ui.MainWindow;
import cz.muni.fi.pv168.project.ui.action.QuitAction;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
//        var errorHandler = new ApplicationErrorHandler();
//        Thread.setDefaultUncaughtExceptionHandler(errorHandler);

        initLookAndFeel();
        EventQueue.invokeLater(() -> {
            try {
                new MainWindow().show();
            } catch (Exception ex) {
                showInitializationFailedDialog(ex);
            }
        });
    }

    private static void initLookAndFeel() {

        try {
            // UITHEME
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Nimbus layout initialization failed", ex);
            throw new IllegalStateException("Nimbus layout initialization failed");
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
    }

    private static void showInitializationFailedDialog(Exception ex) {
        EventQueue.invokeLater(() -> {
            ex.printStackTrace();
            Object[] options = {
                    new JButton(new QuitAction())
                    //new JButton(new NuclearQuitAction())
            };
            JOptionPane.showOptionDialog(
                    null,
                    "Application initialization failed.\nWhat do you want to do?",
                    "Initialization Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    options,
                    options[0]
            );
        });
    }
}
