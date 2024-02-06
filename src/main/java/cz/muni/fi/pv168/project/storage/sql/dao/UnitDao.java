package cz.muni.fi.pv168.project.storage.sql.dao;

import cz.muni.fi.pv168.project.storage.sql.entity.UnitEntity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UnitDao implements DataAccessObject<UnitEntity> {

    private final Connection con;

    public UnitDao(Connection con) {
        this.con = con;
    }


    @Override
    public UnitEntity create(UnitEntity newUnit) {
        var sql = """
                INSERT INTO Unit(
                    guid,
                    unitName,
                    abbreviation,
                    amount,
                    baseUnitId
                )
                VALUES (?, ?, ?, ?, ?);
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, newUnit.guid());
            statement.setString(2, newUnit.unitName());
            statement.setString(3, newUnit.abbreviation());
            statement.setDouble(4, newUnit.amount());
            statement.setLong(5, newUnit.baseUnitId());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long unitId;

                if (keyResultSet.next()) {
                    unitId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + newUnit);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + newUnit);
                }

                return findById(unitId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + newUnit, ex);
        }
    }

    @Override
    public Collection<UnitEntity> findAll() {
        var sql = """
                SELECT id,
                    guid,
                    unitName,
                    abbreviation,
                    amount,
                    baseUnitId
                FROM Unit
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            List<UnitEntity> units = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var unit = unitFromResultSet(resultSet);
                    units.add(unit);
                }
            }

            return units;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all units", ex);
        }
    }

    @Override
    public Optional<UnitEntity> findById(long id) {
        var sql = """
                SELECT id,
                    guid,
                    unitName,
                    abbreviation,
                    amount,
                    baseUnitId
                FROM Unit
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(unitFromResultSet(resultSet));
            } else {
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by id", ex);
        }
    }

    @Override
    public Optional<UnitEntity> findByGuid(String guid) {
        var sql = """
                SELECT id,
                    guid,
                    unitName,
                    abbreviation,
                    amount,
                    baseUnitId
                FROM Unit
                WHERE guid = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(unitFromResultSet(resultSet));
            } else {
                // recipe not found
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load unit by id", ex);
        }
    }

    @Override
    public UnitEntity update(UnitEntity entity) {
        var sql = """
                UPDATE Unit
                SET unitName = ?,
                    abbreviation = ?,
                    amount = ?,
                    baseUnitId = ?
                WHERE id = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.unitName());
            statement.setString(2, entity.abbreviation());
            statement.setDouble(3, entity.amount());
            statement.setLong(4, entity.baseUnitId());
            statement.setLong(5, entity.id());
            statement.executeUpdate();

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Unit not found, id: " + entity.id());
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 Unit (rows=%d) has been updated: %s"
                        .formatted(rowsUpdated, entity));
            }
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update Unit: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Unit WHERE guid = ?";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DataStorageException("Unit not found, guid: " + guid);
            }
            if (rowsUpdated > 1) {
                throw new DataStorageException("More then 1 Unit (rows=%d) has been deleted: %s"
                        .formatted(rowsUpdated, guid));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete Unit, guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Unit";
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all Units", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = """
                SELECT id
                FROM Unit
                WHERE guid = ?
                """;
        try (
                var statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if Unit exists: " + guid, ex);
        }
    }


    private static UnitEntity unitFromResultSet(ResultSet resultSet) throws SQLException {
        return new UnitEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("unitName"),
                resultSet.getString("abbreviation"),
                resultSet.getDouble("amount"),
                resultSet.getInt("baseUnitId")
        );
    }

}
