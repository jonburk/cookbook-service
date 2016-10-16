/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook;

import com.jburk.cookbook.web.RecipeController;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Jonathan Burk
 */
@Configuration
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    register(RecipeController.class);
  }
}
