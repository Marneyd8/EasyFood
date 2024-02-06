package cz.muni.fi.pv168.project.business.service.export.batch;

/**
 * Exception thrown in case there is a problem with a bulk operation.
 */
public class BatchOperationException extends RuntimeException {
    public BatchOperationException(String message) {
        super(message);
    }

    public BatchOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
