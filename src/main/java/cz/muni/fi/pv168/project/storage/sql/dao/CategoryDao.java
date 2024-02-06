package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CategoryDao implements DataAccessObject<CategoryEntity> {
    private final Connection con;

    public CategoryDao(Connection connection) {
        this.con = connection;
    }

    @Override
    public CategoryEntity create(CategoryEntity newCategory) {
        var sql = """
                INSERT INTO Category(
                    guid,
                    categoryName,
                    color
                )
                VALUES (?, ?, ?);
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newCategory.guid());
            statement.setString(2, newCategory.categoryName());
            statement.setInt(3, newCategory.color().getRGB());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long categoryId;

                if (keyResultSet.next()) {
                    categoryId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newCategory);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newCategory);
                }

                return findById(categoryId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newCategory, ex);
        }
    }

    @Override
    public Collection<CategoryEntity> findAll() {
        var sql = """
                SELECT id,
                    guid,
                    categoryName,
                    color
                FROM Category
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            List<CategoryEntity> categories = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var category = categoryFromResultSet(resultSet);
                    categories.add(category);
                }
            }

            return categories;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    @Override
    public Optional<CategoryEntity> findById(long id) {
        var sql = """
                SELECT id,
                    guid,
                    categoryName,
                    color
                FROM Category
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(categoryFromResultSet(resultSet));
            } else {
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load category by id", ex);
        }
    }

    @Override
    public Optional<CategoryEntity> findByGuid(String guid) {
        var sql = """
                SELECT id,
                    guid,
                    categoryName,
                    color
                FROM Category
                WHERE guid = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(categoryFromResultSet(resultSet));
            } else {
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load category by id", ex);
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity entity) {
        var sql = """
                UPDATE Category
                SET categoryName = ?,
                    color = ?
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.categoryName());
            statement.setInt(2, entity.color().getRGB());
            statement.setLong(3, entity.id());
            statement.executeUpdate();

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Category not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 Category (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update Category: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Category WHERE guid = ?";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Category not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 Category (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete Category, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Category";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all Categories", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Category
                WHERE guid = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if Category exists: " + guid, ex);
        }
    }


    private static CategoryEntity categoryFromResultSet(ResultSet resultSet) throws SQLException {
        return new CategoryEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("categoryName"),
                new Color(resultSet.getInt("color"))
        );
    }
}
