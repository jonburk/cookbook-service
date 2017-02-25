/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author Jonathan Burk
 */
@SpringBootApplication
@EnableSwagger2
public class CookbookApplication {

  public static void main(String[] args) {
    SpringApplication.run(CookbookApplication.class, args);
  } 
}
