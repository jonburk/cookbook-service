/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.domain;

import java.io.Serializable;

/**
 * An entity containing a name field that can be used in recipe searches.
 * 
 * @author Jonathan Burk
 */
public interface SearchableNamedEntity extends Serializable {

  String getName();

  void setName(String name);
}
