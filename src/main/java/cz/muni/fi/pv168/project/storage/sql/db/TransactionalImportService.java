package cz.muni.fi.pv168.project.storage.sql.db;

import cz.muni.fi.pv168.project.business.service.export.DataManipulationException;
import cz.muni.fi.pv168.project.business.service.export.ImportService;
import cz.muni.fi.pv168.project.business.service.export.format.Format;
import cz.muni.fi.pv168.project.ui.action.mport.ImportType;

import javax.swing.*;
import java.util.Collection;

public class TransactionalImportService implements ImportService {
    private final ImportService importService;

    private final TransactionExecutor transactionExecutor;

    public TransactionalImportService(ImportService importService, TransactionExecutor transactionExecutor) {
        this.importService = importService;
        this.transactionExecutor = transactionExecutor;
    }

    @Override
    public void importData(String filePath, ImportType importType) {
        try {
            transactionExecutor.executeInTransaction(() -> importService.importData(filePath, importType));
            JOptionPane.showMessageDialog(null, "Import was successful!");
        } catch (DataManipulationException e) {
            throw new DataManipulationException(e.getMessage());
        }
    }

    @Override
    public Collection<Format> getFormats() {
        return importService.getFormats();
    }
}

