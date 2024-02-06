package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.business.model.PreparationTime;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class RecipeDao implements DataAccessObject<RecipeEntity> {

    private final Connection con;

    public RecipeDao(Connection con) {
        this.con = con;
    }

    @Override
    public RecipeEntity create(RecipeEntity newRecipe) {
        var sql = """
                INSERT INTO Recipe(
                    guid,
                    recipeName,
                    prepMinutes,
                    portions,
                    categoryId,
                    description
                )
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newRecipe.guid());
            statement.setString(2, newRecipe.recipeName());
            statement.setInt(3, newRecipe.prepMinutes());
            statement.setInt(4, newRecipe.portions());
            statement.setLong(5, newRecipe.categoryId());
            statement.setString(6, newRecipe.description());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long recipeId;

                if (keyResultSet.next()) {
                    recipeId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newRecipe);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newRecipe);
                }

                return findById(recipeId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newRecipe, ex);
        }
    }

    @Override
    public Collection<RecipeEntity> findAll() {
        var sql = """
                SELECT id,
                    guid,
                    recipeName,
                    prepMinutes,
                    portions,
                    categoryId,
                    description
                FROM Recipe
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            List<RecipeEntity> recipes = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var recipe = recipeFromResultSet(resultSet);
                    recipes.add(recipe);
                }
            }

            return recipes;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all employees", ex);
        }
    }

    @Override
    public Optional<RecipeEntity> findById(long id) {
        var sql = """
                SELECT id,
                    guid,
                    recipeName,
                    prepMinutes,
                    portions,
                    categoryId,
                    description
                FROM Recipe
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
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load employee by id", ex);
        }
    }

    @Override
    public Optional<RecipeEntity> findByGuid(String guid) {
        var sql = """
                SELECT id,
                    guid,
                    recipeName,
                    prepMinutes,
                    portions,
                    categoryId,
                    description
                FROM Recipe
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
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load employee by id", ex);
        }
    }

    @Override
    public RecipeEntity update(RecipeEntity entity) {
        var sql = """
                UPDATE Recipe
                SET recipeName = ?,
                    prepMinutes = ?,
                    portions = ?,
                    categoryId = ?,
                    description = ?
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.recipeName());
            statement.setInt(2, entity.prepMinutes());
            statement.setInt(3, entity.portions());
            statement.setLong(4, entity.categoryId());
            statement.setString(5, entity.description());
            statement.setLong(6, entity.id());
            statement.executeUpdate();

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Recipe not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipe (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipe: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Recipe WHERE guid = ?";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Recipe not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 recipe (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipe, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Recipe";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all recipes", ex);
        }
    }

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
            throw new DataStorageException("Failed to check if Recipe exists: " + guid, ex);
        }
    }


    private static RecipeEntity recipeFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getLong("categoryId"),
                resultSet.getString("recipeName"),
                resultSet.getInt("prepMinutes"),
                resultSet.getInt("portions"),
                resultSet.getString("description")
        );
    }
}
