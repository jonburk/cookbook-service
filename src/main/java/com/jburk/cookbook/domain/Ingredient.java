/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * A recipe ingredient
 * 
 * @author Jonathan Burk
 */
@Entity
@Table(name = "ingredients")
public class Ingredient implements SearchableNamedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ingredientid")
  private Integer id;

  @Column(length = 50)
  private String name;

  private Boolean warn;

  private Boolean staple;

  @Column(name = "ismeat")
  @JsonView(Views.IngredientDetail.class)
  private Boolean meat;

  @Column(name = "isdairy")
  @JsonView(Views.IngredientDetail.class)
  private Boolean dairy;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "recipe_ingredient",
          joinColumns = @JoinColumn(name = "ingredientid", referencedColumnName = "ingredientid"),
          inverseJoinColumns = @JoinColumn(name = "recipeid", referencedColumnName = "recipeid"))
  private Set<Recipe> recipes;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  public Boolean getWarn() {
    return warn;
  }

  public void setWarn(Boolean warn) {
    this.warn = warn;
  }

  public Boolean getStaple() {
    return staple;
  }

  public void setStaple(Boolean staple) {
    this.staple = staple;
  }

  public Boolean getMeat() {
    return meat;
  }

  public void setMeat(Boolean meat) {
    this.meat = meat;
  }

  public Boolean getDairy() {
    return dairy;
  }

  public void setDairy(Boolean dairy) {
    this.dairy = dairy;
  }

  public Set<Recipe> getRecipes() {
    return recipes;
  }

  public void setRecipes(Set<Recipe> recipes) {
    this.recipes = recipes;
  }

}
