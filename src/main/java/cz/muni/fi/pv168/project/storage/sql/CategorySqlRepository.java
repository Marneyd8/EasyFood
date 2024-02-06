package cz.muni.fi.pv168.project.storage.sql;


import cz.muni.fi.pv168.project.business.model.Category;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.project.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

public class CategorySqlRepository implements Repository<Category> {

    private final DataAccessObject<CategoryEntity> categoryDao;
    private final EntityMapper<CategoryEntity, Category> categoryMapper;

    public CategorySqlRepository(
            DataAccessObject<CategoryEntity> categoryDao,
            EntityMapper<CategoryEntity, Category> categoryMapper) {
        this.categoryDao = categoryDao;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> findAll() {
        return categoryDao
                .findAll()
                .stream()
                .map(categoryMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Category newEntity) {
        categoryDao.create(categoryMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Category entity) {
        var existingDepartment = categoryDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Category not found, guid: " + entity.getGuid()));
        var updatedCategory = categoryMapper.mapExistingEntityToDatabase(entity, existingDepartment.id());

        categoryDao.update(updatedCategory);
    }

    @Override
    public void deleteByGuid(String guid) {
        categoryDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        categoryDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return categoryDao.existsByGuid(guid);
    }

    @Override
    public boolean existsByName(String name) {
        return findAll().stream()
                .anyMatch(entity -> entity.getName().equals(name));
    }

    @Override
    public Optional<Category> findByGuid(String guid) {
        return categoryDao
                .findByGuid(guid)
                .map(categoryMapper::mapToBusiness);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryDao
                .findById(id)
                .map(categoryMapper::mapToBusiness);
    }
}
