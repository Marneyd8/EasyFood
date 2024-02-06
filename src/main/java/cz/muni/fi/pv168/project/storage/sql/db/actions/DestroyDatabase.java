package cz.muni.fi.pv168.project.storage.sql.db.actions;

import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;

public final class DestroyDatabase {
    public static void main(String[] args) {
        var dbManager = DatabaseManager.createProductionInstance();
        dbManager.destroySchema();
    }
}
