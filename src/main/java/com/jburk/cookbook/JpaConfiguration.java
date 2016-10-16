/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook;

import com.jburk.cookbook.domain.RecipeRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Jonathan Burk
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = RecipeRepository.class)
public class JpaConfiguration {

}
