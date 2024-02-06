package cz.muni.fi.pv168.project.storage.sql.db.actions;

import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;

public final class CreateDatabase {
    public static void main(String[] args) {
        var dbManager = DatabaseManager.createProductionInstance();
        dbManager.initSchema();
        System.out.println("Database created...");
        System.out.println("Database connection string: " + dbManager.getDatabaseConnectionString());
    }
}
