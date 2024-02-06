package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.ui.action.TabbedPanelContext;

import javax.swing.*;
import java.util.Collection;


/**
 * Receives list of invalid results from validation
 * based on their count, showing desired window
 */

public class DeletePopupDialog {

    private final Collection<String> invalidValidationResults;

    public DeletePopupDialog(
            Collection<String> invalidValidationResults
    ) {
        this.invalidValidationResults = invalidValidationResults;
    }

    public ValidationResult show() {
        if (invalidValidationResults.isEmpty()) {
            int confirm = JOptionPane.showOptionDialog(TabbedPanelContext.getActiveTable(),
                    "Confirm",
                    "Delete confirmation",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, null, null);
            if (confirm != JOptionPane.OK_OPTION) {
                return ValidationResult.failed("denied");
            }
        } else {
            int option = JOptionPane.showConfirmDialog(
                    new JPanel(),
                    String.join("\n", invalidValidationResults),
                    "Are you sure you want to delete?",
                    JOptionPane.YES_NO_OPTION
            );
            if (option != JOptionPane.YES_OPTION) {
                return ValidationResult.failed("denied");
            }
        }
        return ValidationResult.success();
    }
}