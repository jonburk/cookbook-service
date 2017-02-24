/*
 * Copyright (c) 2017 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.utils;

import com.google.common.base.Strings;
import java.io.OutputStream;
import java.net.URI;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 * Image utilities
 * @author Jonathan Burk
 */
public class ImageUtils {
  /**
   * Creates a response for an image byte array
   * @param image the image contents
   * @param placeholder a placeholder image
   * @return a response
   */
  public static Response createResponse(byte[] image, String placeholder) {    
    if (image != null && image.length > 0) {
      return Response.ok().entity((StreamingOutput) (OutputStream output) -> {
        output.write(image);
        output.flush();
      }).build();
    } else if (!Strings.isNullOrEmpty(placeholder)) {
      return Response.temporaryRedirect(URI.create(placeholder)).build();
    } else {
      throw new NotFoundException();
    }
  }
}
