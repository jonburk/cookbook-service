/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.domain;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * The recipe repository
 * 
 * @author Jonathan Burk
 */
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

  @Query("SELECT r from Recipe r"
          /* Filter by title */
          + " WHERE (:title = NULL OR r.title LIKE :title)"
          + " AND (:excludedtitle = NULL OR r.title NOT LIKE :excludedtitle)"
          /* Filter by included tags */
          + " AND (:tagcount = 0L"
          + " OR EXISTS"
          + " (SELECT 1 FROM Tag t"
          + " JOIN t.recipes tr"
          + " WHERE tr.id = r.id"
          + " AND t.name IN :tags"
          + " GROUP BY tr.id"
          + " HAVING COUNT(DISTINCT t.name) = :tagcount))"
          /* Filter by excluded tags */
          + " AND (:excludedtagcount = 0L"
          + " OR NOT EXISTS"
          + " (SELECT 1 FROM Tag t"
          + " JOIN t.recipes tr"
          + " WHERE tr.id = r.id"
          + " AND t.name IN :excludedtags"
          + " GROUP BY tr.id))"
          /* Filter by included ingredients */
          + " AND (:ingredientcount = 0L"
          + " OR EXISTS"
          + " (SELECT 1 FROM Ingredient i"
          + " JOIN i.recipes ir"
          + " WHERE ir.id = r.id"
          + " AND i.name IN :ingredients"
          + " GROUP BY ir.id"
          + " HAVING COUNT(DISTINCT i.name) = :ingredientcount))"
          /* Filter by excluded ingredients */
          + " AND (:excludedingredientcount = 0L"
          + " OR NOT EXISTS"
          + " (SELECT 1 FROM Ingredient i"
          + " JOIN i.recipes ir"
          + " WHERE ir.id = r.id"
          + " AND i.name IN :excludedingredients"
          + " GROUP BY ir.id))"          
          /* Filter by favorites */
          + " AND (:favorite = NULL OR r.favorite = :favorite)")
  Page<Recipe> search(Pageable pgbl,
          @Param("title") String title,
          @Param("excludedtitle") String excludedTitle,
          @Param("tags") Collection<String> tags,
          @Param("tagcount") Long tagCount,
          @Param("excludedtags") Collection<String> excludedTags,
          @Param("excludedtagcount") Long excludedTagCount,
          @Param("ingredients") Collection<String> ingredients,
          @Param("ingredientcount") Long ingredientCount,
          @Param("excludedingredients") Collection<String> excludedIngredients,
          @Param("excludedingredientcount") Long excludedIngredientCount,
          @Param("favorite") Boolean favorite);
}