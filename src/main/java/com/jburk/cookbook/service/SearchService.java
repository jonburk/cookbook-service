/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.service;

import com.jburk.cookbook.domain.IngredientRepository;
import com.jburk.cookbook.domain.Recipe;
import com.jburk.cookbook.domain.RecipeRepository;
import com.jburk.cookbook.domain.SearchableNamedEntity;
import com.jburk.cookbook.domain.SearchableNamedEntityRepository;
import com.jburk.cookbook.domain.TagRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Supports recipe search functionality
 * 
 * @author Jonathan Burk
 */
@Component
public class SearchService {

  private static final String NOT_SEARCH_OPERATOR = "-";
  private static final String FAVORITE = "favorite";
  private static final String VEGAN = "vegan";
  private static final String VEGETARIAN = "vegetarian";
  private static final String WARN = "warn";
  private static final String IN_SEASON = "in season";

  private static final List<String> KEYWORDS = Arrays.asList(FAVORITE, VEGAN, VEGETARIAN, WARN, IN_SEASON);

  @Autowired
  private RecipeRepository recipeRepository;

  @Autowired
  private IngredientRepository ingredientRepository;

  @Autowired
  private TagRepository tagRepository;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  /**
   * Performs a recipe search
   * @param pageRequest paging and sorting options
   * @param terms the set of search terms
   * @return search results
   */
  public Page<Recipe> search(
          Pageable pageRequest,
          Set<String> terms) {

    Page<Recipe> page;

    if (terms == null || terms.isEmpty()) {
      page = recipeRepository.findAll(pageRequest);
    } else {
      terms = terms.stream()
              .map(s -> s.toLowerCase())
              .collect(Collectors.toSet());

      // Extract keywords
      Boolean favorite = extractBoolean(terms, FAVORITE);
      Boolean vegan = extractBoolean(terms, VEGAN);
      Boolean vegetarian = extractBoolean(terms, VEGETARIAN);
      Boolean warn = extractBoolean(terms, WARN);
      Boolean inSeason = extractBoolean(terms, IN_SEASON);

      // Separate included and excluded search terms
      Set<String> excludedTerms = terms.stream()
              .filter(t -> t.startsWith(NOT_SEARCH_OPERATOR))
              .collect(Collectors.toSet());

      terms.removeAll(excludedTerms);

      excludedTerms = excludedTerms.stream()
              .map(t -> t.substring(NOT_SEARCH_OPERATOR.length()))
              .collect(Collectors.toSet());

      // Extract the included and excluded tags and ingredients
      Set<String> tags = extractEntityNames(terms, tagRepository);
      Set<String> excludedTags = extractEntityNames(excludedTerms, tagRepository);
      Set<String> ingredients = extractEntityNames(terms, ingredientRepository);
      Set<String> excludedIngredients = extractEntityNames(excludedTerms, ingredientRepository);

      if (terms.size() > 1 || excludedTerms.size() > 1) {
        // Too many remaining terms. Return an empty list
        return new PageImpl<>(new ArrayList<Recipe>());
      }

      String title = getTitle(terms);
      String excludedTitle = getTitle(excludedTerms);

      log.info(String.format(
              "Title: %s; -Title: %s; Tags: %s; -Tags: %s; Ingredients %s; -Ingredients: %s; "
              + "Favorite: %s; Vegetarian: %s; Vegan: %s; Warn: %s; In Season: %s",
              title,
              excludedTitle,
              String.join(", ", tags),
              String.join(", ", excludedTags),
              String.join(", ", ingredients),
              String.join(", ", excludedIngredients),
              favorite,
              vegetarian,
              vegan,
              warn,
              inSeason));

      // The query will fail if the IN clause list is empty.
      // Get the size of the list and pad it with an empty string.
      long tagCount = getSizeAndPad(tags);
      long excludedTagCount = getSizeAndPad(excludedTags);
      long ingredientCount = getSizeAndPad(ingredients);
      long excludedIngredientCount = getSizeAndPad(excludedIngredients);

      // TODO implement remaining keywords
      page = recipeRepository.search(
              pageRequest,
              title,
              excludedTitle,
              tags,
              tagCount,
              excludedTags,
              excludedTagCount,
              ingredients,
              ingredientCount,
              excludedIngredients,
              excludedIngredientCount,
              favorite);
    }

    return page;
  }

  /**
   * Gets the list of searchable terms to support a type-ahead
   * @param startsWith the type-ahead hint
   * @return list of keywords, ingredients, and tags.
   */
  public List<String> getSearchTerms(String startsWith) {
    List<String> searchTerms = new ArrayList<>();

    searchTerms.addAll(KEYWORDS.stream()
            .filter(k -> k.startsWith(startsWith.toLowerCase()))
            .collect(Collectors.toList()));

    searchTerms.addAll(ingredientRepository.findByNameLike(startsWith + "%").stream()
            .map(i -> i.getName().toLowerCase())
            .collect(Collectors.toList()));

    searchTerms.addAll(tagRepository.findByNameLike(startsWith + "%").stream()
            .map(t -> t.getName().toLowerCase())
            .collect(Collectors.toList()));

    Collections.sort(searchTerms);

    return searchTerms;
  }

  /**
   * Finds and removes a keyword from the search terms.
   * @param terms the set of search terms
   * @param keyword the keyword to find in the search terms
   * @return True if the keyword is included, False if excluded, null if not found
   */
  protected static Boolean extractBoolean(Set<String> terms, String keyword) {
    Boolean result = null;
    String notKeyword = NOT_SEARCH_OPERATOR + keyword;

    if (terms.contains(keyword)) {
      result = true;
      terms.remove(keyword);
    } else if (terms.contains(notKeyword)) {
      result = false;
      terms.remove(notKeyword);
    }

    return result;
  }

  /**
   * Finds and removes entity names from a list of search terms
   * @param <T> the entity type
   * @param terms the set of search terms
   * @param repository a repository containing named entities
   * @return the entity names found in the search terms
   */
  protected static <T extends SearchableNamedEntity> Set<String> extractEntityNames(
          Set<String> terms,
          SearchableNamedEntityRepository<T, ?> repository) {

    Set<String> result = StreamSupport.stream(repository.findByNameInIgnoreCase(terms).spliterator(), false)
            .map(e -> e.getName().toLowerCase())
            .collect(Collectors.toSet());

    terms.removeAll(result);

    return result;
  }

  /**
   * Gets the count of the search terms. If zero, adds an empty string as the only search term.
   * @param terms the set of search terms
   * @return the count of search terms
   */
  protected static long getSizeAndPad(Set<String> terms) {
    long result = (long) terms.size();

    if (result == 0L) {
      terms.add("");
    }

    return result;
  }

  /**
   * Returns the first search term and surrounds it with wildcards.
   * @param terms the set of search terms
   * @return the first search term, or null if there are none
   */
  protected static String getTitle(Set<String> terms) {
    return terms.isEmpty() ? null : String.format("%%%s%%", terms.iterator().next());
  }
}
