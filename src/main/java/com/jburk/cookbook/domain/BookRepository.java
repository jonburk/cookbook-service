/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The book depository
 * 
 * @author Jonathan Burk
 */
public interface BookRepository extends JpaRepository<Book, Integer> {

}
