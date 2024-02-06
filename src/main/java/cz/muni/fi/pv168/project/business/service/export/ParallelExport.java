package cz.muni.fi.pv168.project.business.service.export;

import cz.muni.fi.pv168.project.business.service.export.batch.Batch;
import cz.muni.fi.pv168.project.business.service.export.batch.BatchXmlExporter;
import cz.muni.fi.pv168.project.ui.MainWindow;

import javax.swing.*;
import java.io.File;

/**
 * @author Filip Skvara
 */
public class ParallelExport extends SwingWorker<Void,Object> {

    private final File file;
    public ParallelExport(File file) {
        this.file = file;
    }
    @Override
    protected Void doInBackground() {
        BatchXmlExporter exportBatch = new BatchXmlExporter();
        Batch batch = new Batch(MainWindow.commonDependencyProvider.getRecipeCrudService().findAll(),
                MainWindow.commonDependencyProvider.getIngredientCrudService().findAll(),
                MainWindow.commonDependencyProvider.getCustomUnitCrudService().findAll(),
                MainWindow.commonDependencyProvider.getCategoryCrudService().findAll());
        exportBatch.exportBatch(batch, file.getAbsolutePath() + ".xml");
        System.out.println("Selected file: " + file.getAbsolutePath() + ".xml");
        return null;
    }

    @Override
    protected void done() {
        JOptionPane.showMessageDialog(null, "Export has successfully finished.");
        super.done();
    }

}
