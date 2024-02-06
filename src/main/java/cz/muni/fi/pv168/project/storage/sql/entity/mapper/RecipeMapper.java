package cz.muni.fi.pv168.project.storage.sql.entity.mapper;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.RecipeEntity;

public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {

    private final DataAccessObject<CategoryEntity> categoryDao;
    private final CategoryMapper categoryMapper;

    public RecipeMapper(
            DataAccessObject<CategoryEntity> categoryDao,
            CategoryMapper categoryMapper
    ) {
        this.categoryDao = categoryDao;
        this.categoryMapper = categoryMapper;
    }


    @Override
    public Recipe mapToBusiness(RecipeEntity entity) {
        var category = categoryDao
                .findById(entity.categoryId())
                .map(categoryMapper::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("Category not found, id: " +
                        entity.categoryId()));
        return new Recipe(
                entity.guid(),
                entity.recipeName(),
                category,
                entity.prepMinutes(),
                entity.portions(),
                entity.description());
    }

    @Override
    public RecipeEntity mapNewEntityToDatabase(Recipe entity) {
        var categoryEntity = categoryDao
                .findByGuid(entity.getCategory().getGuid())
                .orElseThrow(() -> new DataStorageException("Category not found, guid: " +
                        entity.getCategory().getGuid()));
        return new RecipeEntity(
                entity.getGuid(),
                categoryEntity.id(),
                entity.getRecipeName(),
                entity.getPrepMinutes(),
                entity.getPortions(),
                entity.getDescription());
    }

    @Override
    public RecipeEntity mapExistingEntityToDatabase(Recipe entity, Long dbId) {
        var categoryEntity = categoryDao
                .findByGuid(entity.getCategory().getGuid())
                .orElseThrow(() -> new DataStorageException("Category not found, guid: " +
                        entity.getCategory().getGuid()));
        return new RecipeEntity(
                dbId,
                entity.getGuid(),
                categoryEntity.id(),
                entity.getRecipeName(),
                entity.getPrepMinutes(),
                entity.getPortions(),
                entity.getDescription());
    }
}
