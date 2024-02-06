package cz.muni.fi.pv168.project.storage.sql.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void init(Connection connection, String filepath) {
        executeSqlQueries(connection, loadSqlFile(filepath));
    }

    private static String loadSqlFile(String filepath) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        DatabaseInitializer.class.getClassLoader().getResourceAsStream(filepath)
                )
        )) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeSqlQueries(Connection connection, String sqlContent) {
        try (Statement statement = connection.createStatement()) {
            String[] queries = sqlContent.split(";");

            for (String query : queries) {
                statement.addBatch(query.trim());
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
