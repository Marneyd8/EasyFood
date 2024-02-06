// PRESERVE ORDER

--
-- Category table definition
--
CREATE TABLE IF NOT EXISTS "Category"
(
    `id`           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`         VARCHAR      NOT NULL UNIQUE,
    `categoryName` VARCHAR(150) NOT NULL,
    `color`        INT          NOT NULL,
    `createdAt`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--
-- BaseUnit table definition
--
--CREATE TABLE IF NOT EXISTS "BaseUnit"
--(
--    `id`           BIGINT PRIMARY KEY,
--    `guid`         VARCHAR      NOT NULL UNIQUE,
--    `baseUnitName` VARCHAR(150) NOT NULL,
--    `abbreviation` VARCHAR(150) NOT NULL
--);

--
-- CustomUnit table definition
--
CREATE TABLE IF NOT EXISTS "Unit"
(
    `id`           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`         VARCHAR      NOT NULL UNIQUE,
    `unitName`     VARCHAR(150) NOT NULL,
    `abbreviation` VARCHAR(150) NOT NULL,
    `amount`       DOUBLE       NOT NULL,
    `baseUnitId`   INT          NOT NUll,
    `createdAt`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--
-- Ingredient table definition
--
CREATE TABLE IF NOT EXISTS "Ingredient"
(
    `id`               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`             VARCHAR      NOT NULL UNIQUE,
    `ingredientName`   VARCHAR(150) NOT NULL,
    `nutritionalValue` INT          NOT NULL,
    `createdAt`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--
-- Recipe table definition
--
CREATE TABLE IF NOT EXISTS "Recipe"
(
    `id`               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`             VARCHAR      NOT NULL UNIQUE,
    `recipeName`       VARCHAR(150) NOT NULL,
    `prepMinutes`      INT          NOT NULL,
    `portions`         INT          NOT NULL,
    `categoryId`       BIGINT REFERENCES "Category" (`id`) ON DELETE CASCADE,
    `description`      VARCHAR(255)          DEFAULT 'No recipe description',
    `createdAt`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "AddedIngredient"
(
    `id`           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`         VARCHAR   NOT NULL UNIQUE,
    `ingredientId` BIGINT REFERENCES "Ingredient" (`id`) ON DELETE CASCADE,
    `recipeId`     BIGINT REFERENCES "Recipe" (`id`) ON DELETE CASCADE,
    `unitId`       BIGINT REFERENCES "Unit" (`id`) ON DELETE CASCADE,
    `quantity`     DOUBLE       NOT NULL,
    `createdAt`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `baseUnitId`   INT
);

-- CREATE TRIGGER delete_added_ingredients
-- BEFORE DELETE ON "Recipe"
-- FOR EACH ROW
-- BEGIN
--     DELETE FROM "AddedIngredient" WHERE "recipeId" == OLD.recipeId

--
-- Initialization of base units
--
--MERGE INTO "Unit" ( "guid", "unitName", "abbreviation","amount","baseUnitId")
--    VALUES ( '"bu-kg"', 'KILOGRAM', 'kg',0,0),
--           ( 'bu-gg', 'milliliter', 'g',0,1),
--           ( 'bu-ml', 'liter', 'ml',0,2),
--           ( 'bu-ll', 'gram', 'l',0,3),
--          ( 'bu-pcs', 'kilogram', 'pcs',0,4);
