/*
 * Copyright (c) 2017 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.service;

import com.jburk.cookbook.domain.Recipe;
import com.jburk.cookbook.domain.RecipeRepository;
import javax.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Supports recipe functions
 * @author Jonathan Burk
 */
@Component
public class RecipeService {
  @Autowired
  private RecipeRepository recipeRepository;
  
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  
  /**
   * Gets a recipe
   * @param id the recipe ID
   * @return a recipe
   */
  public Recipe getRecipeById(int id) {
    Recipe recipe = recipeRepository.findOne(id);
    
    if (recipe == null) {
      log.warn(String.format("Recipe %d not found", id));
      throw new NotFoundException();
    }
    
    return recipe;
  }
  
  /**
   * Gets a recipe thumbnail image
   * @param id the recipe ID
   * @return the thumbnail image
   */
  public byte[] getThumbnailById(int id) {
    return this.getRecipeById(id).getThumbnail();
  }
  
  /**
   * Gets a recipe photo
   * @param id the recipe ID
   * @return the recipe image
   */
  public byte[] getPhotoById(int id) {
    return this.getRecipeById(id).getPhoto();
  }
}
