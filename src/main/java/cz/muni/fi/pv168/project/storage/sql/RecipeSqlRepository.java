package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

public class RecipeSqlRepository implements Repository<Recipe> {

    private final DataAccessObject<RecipeEntity> recipeDao;
    private final EntityMapper<RecipeEntity, Recipe> recipeMapper;

    public RecipeSqlRepository(
            DataAccessObject<RecipeEntity> recipeDao,
            EntityMapper<RecipeEntity, Recipe> recipeMapper) {
        this.recipeDao = recipeDao;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeDao
                .findAll()
                .stream()
                .map(recipeMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Recipe newEntity) {
        recipeDao.create(recipeMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Recipe entity) {
        var existingDepartment = recipeDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Recipe not found, guid: " + entity.getGuid()));
        var updatedRecipe = recipeMapper.mapExistingEntityToDatabase(entity, existingDepartment.id());

        recipeDao.update(updatedRecipe);
    }

    @Override
    public void deleteByGuid(String guid) {
        recipeDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        recipeDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return recipeDao.existsByGuid(guid);
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public Optional<Recipe> findByGuid(String guid) {
        return recipeDao
                .findByGuid(guid)
                .map(recipeMapper::mapToBusiness);
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        return recipeDao
                .findById(id)
                .map(recipeMapper::mapToBusiness);
    }
}
