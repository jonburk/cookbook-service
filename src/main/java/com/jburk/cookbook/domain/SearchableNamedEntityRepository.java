/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * A repository for named entities used in recipe searches.
 * 
 * @author Jonathan Burk
 * @param <T> The entity type
 * @param <ID> The entity primary key type
 */
@NoRepositoryBean
public interface SearchableNamedEntityRepository<T extends SearchableNamedEntity, ID extends Serializable> extends JpaRepository<T, ID> {

  List<T> findByNameLike(String name);

  List<T> findByNameInIgnoreCase(Set<String> names);
}
