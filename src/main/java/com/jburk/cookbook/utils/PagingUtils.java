/*
 * Copyright (c) 2016 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.utils;

import javax.ws.rs.core.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utilities for paging and sorting functionality.
 * 
 * @author Jonathan Burk
 */
public class PagingUtils {

  private static final String TOTAL_COUNT = "X-Total-Count";

  /**
   * Creates a page request.
   * @param offset the record set offset
   * @param limit the maximum number of records to return
   * @param sort sorting options
   * @return a page request
   */
  public static Pageable createRequest(Integer offset, Integer limit, Sort sort) {
    if (offset == null) {
      offset = 0;
    }

    if (limit == null) {
      limit = Integer.MAX_VALUE;
    }

    return new PageRequest(offset / limit, limit, sort);
  }

  /**
   * Builds a Response for a page of results.
   * @param <T> The result entity type
   * @param page The results page
   * @return a Response containing the results and X-Total-Count header
   */
  public static <T> Response build(Page<T> page) {
    return Response.ok(page.getContent())
            .header(TOTAL_COUNT, page.getTotalElements())
            .build();
  }
}
