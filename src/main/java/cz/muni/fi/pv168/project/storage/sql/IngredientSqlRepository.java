package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

public class IngredientSqlRepository implements Repository<Ingredient> {

    private final DataAccessObject<IngredientEntity> ingredientDao;
    private final EntityMapper<IngredientEntity, Ingredient> ingredientMapper;

    public IngredientSqlRepository(
            DataAccessObject<IngredientEntity> ingredientDao,
            EntityMapper<IngredientEntity, Ingredient> ingredientMapper) {
        this.ingredientDao = ingredientDao;
        this.ingredientMapper = ingredientMapper;
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientDao
                .findAll()
                .stream()
                .map(ingredientMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Ingredient newEntity) {
        ingredientDao.create(ingredientMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Ingredient entity) {
        var existingDepartment = ingredientDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Ingredient not found, guid: " + entity.getGuid()));
        var updatedIngredient = ingredientMapper.mapExistingEntityToDatabase(entity, existingDepartment.id());

        ingredientDao.update(updatedIngredient);
    }

    @Override
    public void deleteByGuid(String guid) {
        ingredientDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        ingredientDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return ingredientDao.existsByGuid(guid);
    }

    @Override
    public boolean existsByName(String name) {
        return findAll().stream()
                .anyMatch(entity -> entity.getName().equals(name));
    }

    @Override
    public Optional<Ingredient> findByGuid(String guid) {
        return ingredientDao
                .findByGuid(guid)
                .map(ingredientMapper::mapToBusiness);
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        return ingredientDao
                .findById(id)
                .map(ingredientMapper::mapToBusiness);
    }


}
