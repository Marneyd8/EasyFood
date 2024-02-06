package cz.muni.fi.pv168.project.ui;

import cz.muni.fi.pv168.project.ui.action.QuitAction;

import javax.swing.*;
import java.awt.*;

public class ApplicationErrorHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // TODO: Handle exceptions better
        showGeneralError("Oops something went wrong!", true);
    }

    private static void showGeneralError(String message, boolean isFatal) {
        final String title = isFatal ? "Fatal Application Error" : "Application Error";
        final Object[] options = getOptionsForDialog(isFatal);

        EventQueue.invokeLater(() ->
                JOptionPane.showOptionDialog(
                        null,
                        message,
                        title,
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        options,
                        null
                ));
    }

    private static Object[] getOptionsForDialog(boolean isFatal) {
        if (!isFatal) {
            return null; // use default
        }

        return new Object[]{
                new JButton(new QuitAction())
        };
    }
}