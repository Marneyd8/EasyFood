package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class IngredientDao implements DataAccessObject<IngredientEntity> {
    private final Connection con;

    public IngredientDao(Connection con) {
        this.con = con;
    }


    @Override
    public IngredientEntity create(IngredientEntity newIngredient) {
        var sql = """
                INSERT INTO Ingredient(
                    guid,
                    ingredientName,
                    nutritionalValue
                )
                VALUES (?, ?, ?);
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newIngredient.guid());
            statement.setString(2, newIngredient.ingredientName());
            statement.setInt(3, newIngredient.nutritionalValue());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long ingredientId;

                if (keyResultSet.next()) {
                    ingredientId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newIngredient);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newIngredient);
                }

                return findById(ingredientId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newIngredient, ex);
        }
    }

    @Override
    public Collection<IngredientEntity> findAll() {
        var sql = """
                SELECT id,
                    guid,
                    ingredientName,
                    nutritionalValue
                FROM Ingredient
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            List<IngredientEntity> ingredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ingredient = recipeFromResultSet(resultSet);
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all ingredients", ex);
        }
    }

    @Override
    public Optional<IngredientEntity> findById(long id) {
        var sql = """
                SELECT id,
                    guid,
                    ingredientName,
                    nutritionalValue
                FROM Ingredient
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                // ingredient not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by id", ex);
        }
    }

    @Override
    public Optional<IngredientEntity> findByGuid(String guid) {
        var sql = """
                SELECT id,
                    guid,
                    ingredientName,
                    nutritionalValue
                FROM Ingredient
                WHERE guid = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                // ingredient not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by id", ex);
        }
    }

    @Override
    public IngredientEntity update(IngredientEntity entity) {
        var sql = """
                UPDATE Ingredient
                SET ingredientName = ?,
                    nutritionalValue = ?
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.ingredientName());
            statement.setInt(2, entity.nutritionalValue());
            statement.setLong(3, entity.id());
            statement.executeUpdate();

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ingredient not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 Ingredient (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update Ingredient: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Ingredient WHERE guid = ?";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Ingredient not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 Ingredient (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete Ingredient, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Ingredient";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all Ingredients", ex);
        }
    }

    // TODO: Recipe v query ?
    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Recipe
                WHERE guid = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if recipe exists: " + guid, ex);
        }
    }


    private static IngredientEntity recipeFromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("ingredientName"),
                resultSet.getInt("nutritionalValue")
        );
    }
}
