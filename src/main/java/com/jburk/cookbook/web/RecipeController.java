/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.jburk.cookbook.domain.Recipe;
import com.jburk.cookbook.domain.Views;
import com.jburk.cookbook.service.SearchService;
import com.jburk.cookbook.utils.PagingUtils;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * The recipe REST service
 * 
 * @author Jonathan Burk
 */
@Path("/recipes")
@Component
public class RecipeController {

  @Autowired
  private SearchService searchService;
  
  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @JsonView(Views.SearchResults.class)
  public Response getAll(
          @QueryParam("offset") Integer offset,
          @QueryParam("limit") Integer limit,
          @QueryParam("search") Set<String> terms) {

    log.info(String.format("Recipe search for %s", String.join(", ", terms)));
    
    Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, Recipe.FAVORITE_FIELD));
    Pageable pageRequest = PagingUtils.createRequest(offset, limit, sort);

    return PagingUtils.build(searchService.search(pageRequest, terms));
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("search-terms")
  public List<String> getSearchTerms(@QueryParam("startsWith") String startsWith) {
    if (startsWith == null) {
      startsWith = "";
    }

    return searchService.getSearchTerms(startsWith);
  }
}
