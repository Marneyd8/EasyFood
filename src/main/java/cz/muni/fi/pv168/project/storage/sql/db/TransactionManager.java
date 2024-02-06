package cz.muni.fi.pv168.project.storage.sql.db;

/**
 * Manages transactions
 */
public interface TransactionManager {

    /**
     * Begins new transaction
     */
    Transaction beginTransaction();

    /**
     * @return active {@link ConnectionHandler} instance
     */
    ConnectionHandler getConnectionHandler();

    /**
     * @return true if transaction is active, false otherwise
     */
    boolean hasActiveTransaction();
}
