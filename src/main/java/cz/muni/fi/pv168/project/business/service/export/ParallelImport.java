package cz.muni.fi.pv168.project.business.service.export;

import cz.muni.fi.pv168.project.business.service.export.batch.BatchXmlExporter;
import cz.muni.fi.pv168.project.business.service.export.importer.BatchXmlImporter;
import cz.muni.fi.pv168.project.ui.MainWindow;
import cz.muni.fi.pv168.project.ui.action.ActionFactory;
import cz.muni.fi.pv168.project.ui.action.mport.ImportAction;
import cz.muni.fi.pv168.project.ui.action.mport.ImportType;
import cz.muni.fi.pv168.project.ui.listeners.StatisticsUpdater;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * @author Filip Skvara
 */
public class ParallelImport extends SwingWorker<Void, Object> {
    private final File file;
    private final Runnable callback;
    private final ImportType importType;

    public ParallelImport(File file, Runnable callback, ImportType importType) {
        this.file = file;
        this.callback = callback;
        this.importType = importType;
    }

    @Override
    protected Void doInBackground() {
        MainWindow.commonDependencyProvider.getTransactionalImportService()
                .importData(file.getAbsolutePath(), importType);
        callback.run();
        System.out.println("Selected file: " + file.getAbsolutePath());
        return null;
    }

    @Override
    protected void done() {
//        JOptionPane.showMessageDialog(null, "Import has successfully finished.");
        StatisticsUpdater.reload();
        super.done();
    }
}
