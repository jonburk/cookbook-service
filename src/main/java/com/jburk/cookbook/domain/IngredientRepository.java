/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.domain;

/**
 * The ingredient repository
 * 
 * @author Jonathan Burk
 */
public interface IngredientRepository extends SearchableNamedEntityRepository<Ingredient, Integer> {

}
