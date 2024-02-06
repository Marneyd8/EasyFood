package cz.muni.fi.pv168.project.storage.sql.entity;

public record RecipeEntity(
        Long id,
        String guid,
        long categoryId,
        String recipeName,
        int prepMinutes,
        int portions,
        String description) {
    public RecipeEntity(
            Long id,
            String guid,
            long categoryId,
            String recipeName,
            int prepMinutes,
            int portions,
            String description
    ) {
        this.id = id;
        this.guid = guid;
        this.categoryId = categoryId;
        this.recipeName = recipeName;
        this.prepMinutes = prepMinutes;
        this.portions = portions;
        this.description = description;
    }

    public RecipeEntity(String guid, long categoryId, String recipeName,
                        int prepMinutes, int portions, String description) {
        this(null, guid, categoryId, recipeName, prepMinutes, portions, description);
    }
}
