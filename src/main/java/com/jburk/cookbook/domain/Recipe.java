/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A recipe for something tasty
 * 
 * @author Jonathan Burk
 */
@Entity
@Table(name = "recipes")
public class Recipe implements Serializable {

  public static final String FAVORITE_FIELD = "favorite";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "recipeid")
  private Integer id;

  @Column(length = 100)
  private String title;

  private Integer page;

  @Lob
  @JsonView(Views.RecipeDetail.class)
  private String notes;

  @Column(name = FAVORITE_FIELD)
  private Boolean favorite;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "lastmade")
  private Date lastMade;

  @ManyToOne
  @JoinColumn(name = "bookid")
  private Book book;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "recipe_ingredient",
          joinColumns = @JoinColumn(name = "recipeid", referencedColumnName = "recipeid"),
          inverseJoinColumns = @JoinColumn(name = "ingredientid", referencedColumnName = "ingredientid"))
  private Set<Ingredient> ingredients;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
          name = "recipe_tag",
          joinColumns = @JoinColumn(name = "recipeid", referencedColumnName = "recipeid"),
          inverseJoinColumns = @JoinColumn(name = "tagid", referencedColumnName = "tagid"))
  private Set<Tag> tags;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String Title) {
    this.title = Title;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer Page) {
    this.page = Page;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String Notes) {
    this.notes = Notes;
  }

  public Boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(Boolean Favorite) {
    this.favorite = Favorite;
  }

  public Date getLastMade() {
    return lastMade;
  }

  public void setLastMade(Date LastMade) {
    this.lastMade = LastMade;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Set<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(Set<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public Set<Tag> getTags() {
    return tags;
  }

  public void setTags(Set<Tag> tags) {
    this.tags = tags;
  }

}
