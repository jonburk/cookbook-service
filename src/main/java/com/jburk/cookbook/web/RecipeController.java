/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.jburk.cookbook.domain.Recipe;
import com.jburk.cookbook.domain.Views;
import com.jburk.cookbook.service.RecipeService;
import com.jburk.cookbook.service.SearchService;
import com.jburk.cookbook.utils.ImageUtils;
import com.jburk.cookbook.utils.PagingUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@Api(value = "Recipes")
public class RecipeController {

  private static final String MISSING_THUMBNAIL = "/images/NoThumbnail.png";
  private static final String MISSING_PHOTO = "/images/NoPhoto.png";
  
  @Autowired
  private SearchService searchService;
  
  @Autowired
  private RecipeService recipeService;
  
  private final Logger log = LoggerFactory.getLogger(this.getClass());  
  
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @JsonView(Views.RecipeDetail.class)
  @ApiOperation(value = "Gets a recipe")
  @ApiResponses({
    @ApiResponse(
            code = 200,
            message = "Success",
            response = Recipe.class),
    @ApiResponse(
            code = 404,
            message = "Not found")
  })
  public Recipe getById(@ApiParam("Recipe ID") @PathParam("id") int id) {
    return recipeService.getRecipeById(id);
  }
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @JsonView(Views.SearchResults.class)
  @ApiOperation(
          value = "Search recipes", 
          notes = "Searches recipe titles, ingredients, and tags."
                  + " Prefix a search term with '-' to exclude it.")
  @ApiResponses({
    @ApiResponse(
            code = 200,
            message = "Success",
            response = Recipe.class,  
            responseContainer = "Set",
            responseHeaders = @ResponseHeader(          
              name = PagingUtils.TOTAL_COUNT, 
              response = Long.class,
              description = "Total number of results matching the search term(s)."))
  })
  public Response getAll(
          @ApiParam(value = "Search result offset", defaultValue = "0") @QueryParam("offset") Integer offset,
          @ApiParam(value = "Maximum number of results", defaultValue = "10") @QueryParam("limit") Integer limit,
          @ApiParam(value = "Search terms.", allowMultiple = true) @QueryParam("search") Set<String> terms) {

    log.info(String.format("Recipe search for %s", String.join(", ", terms)));
    
    Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, Recipe.FAVORITE_FIELD));
    Pageable pageRequest = PagingUtils.createRequest(offset, limit, sort);

    return PagingUtils.build(searchService.search(pageRequest, terms));
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("search-terms")
  @ApiOperation("Gets suggested search terms")
  @ApiResponses({
    @ApiResponse(
            code = 200,
            message = "Success",
            response = String.class,  
            responseContainer = "Set")
  })  
  public List<String> getSearchTerms(
          @ApiParam("Filters the suggested terms") @QueryParam("startsWith") String startsWith) {
    if (startsWith == null) {
      startsWith = "";
    }

    return searchService.getSearchTerms(startsWith);
  }
  
  @GET
  @Produces("image/png")
  @Path("{id}/thumbnail")
  @ApiOperation("Gets a recipe thumbnail")
  @ApiResponses({
    @ApiResponse(
            code = 200,
            message = "Success"),
    @ApiResponse(
            code = 404,
            message = "Not found")
  })  
  public Response getThumbnail(@ApiParam("Recipe ID") @PathParam("id") int id) {
    return ImageUtils.createResponse(recipeService.getThumbnailById(id), MISSING_THUMBNAIL);
  }
  
  @GET
  @Produces("image/png")
  @Path("{id}/photo")
  @ApiOperation("Gets a recipe photo")
  @ApiResponses({
    @ApiResponse(
            code = 200,
            message = "Success"),
    @ApiResponse(
            code = 404,
            message = "Not found")
  }) 
  public Response getPhoto(@ApiParam("Recipe ID") @PathParam("id") int id) {
    return ImageUtils.createResponse(recipeService.getPhotoById(id), MISSING_PHOTO);
  }
}
