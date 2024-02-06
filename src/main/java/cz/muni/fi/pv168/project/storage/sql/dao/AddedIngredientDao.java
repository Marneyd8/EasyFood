package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.entity.AddedIngredientEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class AddedIngredientDao implements DataAccessObject<AddedIngredientEntity> {

    private final Connection con;

    public AddedIngredientDao(Connection con) {
        this.con = con;
    }

    @Override
    public AddedIngredientEntity create(AddedIngredientEntity newAddedIngredient) {
        var sql = """
                INSERT INTO AddedIngredient(
                    guid,
                    ingredientId,
                    recipeId,
                    unitId,
                    quantity,
                    baseUnitId
                )
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newAddedIngredient.guid());
            statement.setLong(2, newAddedIngredient.ingredientId());
            statement.setLong(3, newAddedIngredient.recipeId());
            if (newAddedIngredient.unitId() == -1) {
                statement.setNull(4, Types.BIGINT);
            } else {
                statement.setLong(4, newAddedIngredient.unitId());
            }
            //newAddedIngredient.unitId() == -1 ? statement.setNull(4, Types.BIGINT) : statement.setLong(4, newAddedIngredient.unitId());
            statement.setDouble(5, newAddedIngredient.quantity());
            statement.setInt(6, newAddedIngredient.baseUnit());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long addedIngredientId;

                if (keyResultSet.next()) {
                    addedIngredientId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newAddedIngredient);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newAddedIngredient);
                }

                return findById(addedIngredientId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newAddedIngredient, ex);
        }
    }

    public Collection<AddedIngredientEntity> findByRecipeId(Long recipeId) {
        var sql = """
                SELECT id,
                    guid,
                    ingredientId,
                    recipeId,
                    unitId,
                    quantity,
                    baseUnitId
                FROM AddedIngredient
                WHERE recipeId = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, recipeId);
            List<AddedIngredientEntity> addedIngredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var addedIngredient = addedIngredientFromResultSet(resultSet);
                    addedIngredients.add(addedIngredient);
                }
            }

            return addedIngredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    @Override
    public Collection<AddedIngredientEntity> findAll() {
        var sql = """
                SELECT id,
                    guid,
                    ingredientId,
                    recipeId,
                    unitId,
                    quantity,
                    baseUnitId
                FROM AddedIngredient
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            List<AddedIngredientEntity> addedIngredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var addedIngredient = addedIngredientFromResultSet(resultSet);
                    addedIngredients.add(addedIngredient);
                }
            }

            return addedIngredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all categories", ex);
        }
    }

    @Override
    public Optional<AddedIngredientEntity> findById(long id) {
        var sql = """
                SELECT id,
                        guid,
                        ingredientId,
                        recipeId,
                        unitId,
                        quantity,
                        baseUnitId
                FROM AddedIngredient
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(addedIngredientFromResultSet(resultSet));
            } else {
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load addedIngredient by id", ex);
        }
    }

    @Override
    public Optional<AddedIngredientEntity> findByGuid(String guid) {
        var sql = """
                SELECT id,
                        recipeId
                        guid
                        ingredientId
                        unitId
                        quantity
                        baseUnitId
                FROM AddedIngredient
                WHERE guid = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(addedIngredientFromResultSet(resultSet));
            } else {
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load addedIngredient by id", ex);
        }
    }


    @Override
    public AddedIngredientEntity update(AddedIngredientEntity entity) {
        var sql = """
                UPDATE AddedIngredient
                SET guid = ?,
                        ingredientId = ?,
                        recipeId = ?,
                        unitId = ?,
                        quantity = ?,
                        baseUnitId = ?
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setLong(2, entity.ingredientId());
            statement.setLong(3, entity.recipeId());
            if (entity.unitId() == -1) {
                statement.setNull(4, Types.BIGINT);
            } else {
                statement.setLong(4, entity.unitId());
            }
            statement.setLong(4, entity.unitId() == -1 ? null : entity.unitId());
            statement.setDouble(5, entity.quantity());
            statement.setInt(6, entity.baseUnit());
            statement.executeUpdate();

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("AddedIngredient not found, id: " + entity.recipeId());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 addedIngredient (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update addedIngredient: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM AddedIngredient WHERE guid = ?";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("AddedIngredient not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 addedIngredient (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete addedIngredient, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM AddedIngredient";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all addedIngredients", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM AddedIngredient
                WHERE guid = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if addedIngredient exists: " + guid, ex);
        }
    }


    private static AddedIngredientEntity addedIngredientFromResultSet(ResultSet resultSet) throws SQLException {
        return new AddedIngredientEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getLong("ingredientId"),
                resultSet.getLong("recipeId"),
                resultSet.getLong("unitId"),
                resultSet.getDouble("quantity"),
                resultSet.getInt("baseUnitId")
        );
    }
}