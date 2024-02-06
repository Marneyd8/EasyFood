package cz.muni.fi.pv168.project.storage.sql.db;

import java.io.Closeable;

/**
 * Transaction Handling
 */
public interface Transaction extends Closeable {

    /**
     * @return active {@link ConnectionHandler} instance
     */
    ConnectionHandler connection();

    /**
     * Commits active transaction
     */
    void commit();

    /**
     * Closes active connection
     */
    void close();

    /**
     * Returns true if connection is closed
     */
    boolean isClosed();
}
