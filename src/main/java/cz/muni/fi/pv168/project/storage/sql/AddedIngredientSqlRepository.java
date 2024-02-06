package cz.muni.fi.pv168.project.storage.sql;

import cz.muni.fi.pv168.project.business.model.AddedIngredient;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.AddedIngredientDao;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.project.storage.sql.entity.AddedIngredientEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddedIngredientSqlRepository implements Repository<AddedIngredient> {

    private final AddedIngredientDao addedIngredientDao;
    private final EntityMapper<AddedIngredientEntity, AddedIngredient> addedIngredientMapper;
    private final RecipeDao recipeDao;

    public AddedIngredientSqlRepository(
            AddedIngredientDao addedIngredientDao,
            EntityMapper<AddedIngredientEntity, AddedIngredient> addedIngredientMapper,
            RecipeDao recipeDao
    ) {
        this.addedIngredientDao = addedIngredientDao;
        this.addedIngredientMapper = addedIngredientMapper;
        this.recipeDao = recipeDao;
    }

    @Override
    public List<AddedIngredient> findAll() {
        return addedIngredientDao
                .findAll()
                .stream()
                .map(addedIngredientMapper::mapToBusiness)
                .toList();
    }

    public List<AddedIngredient> findByRecipeGuid(String recipeGuid) {
        if (recipeGuid == null) {
            return new ArrayList<>();
        }
        RecipeEntity recipe = recipeDao.findByGuid(recipeGuid)
                .orElseThrow(() -> new DataStorageException("Recipe with guid: " + recipeGuid + "doesn't exists"));

        return addedIngredientDao
                .findByRecipeId(recipe.id())
                .stream()
                .map(addedIngredientMapper::mapToBusiness)
                .toList();
    }


    @Override
    public void create(AddedIngredient newEntity) {
        addedIngredientDao.create(addedIngredientMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(AddedIngredient entity) {
        var existingDepartment = addedIngredientDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Category not found, guid: " + entity.getGuid()));
        var updateAddedIngredient = addedIngredientMapper.mapExistingEntityToDatabase(entity, existingDepartment.id());

        addedIngredientDao.update(updateAddedIngredient);
    }

    @Override
    public void deleteByGuid(String guid) {
        addedIngredientDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        addedIngredientDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return addedIngredientDao.existsByGuid(guid);
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public Optional<AddedIngredient> findByGuid(String guid) {
        return addedIngredientDao
                .findByGuid(guid)
                .map(addedIngredientMapper::mapToBusiness);
    }

    @Override
    public Optional<AddedIngredient> findById(Long id) {
        return addedIngredientDao
                .findById(id)
                .map(addedIngredientMapper::mapToBusiness);
    }
}
