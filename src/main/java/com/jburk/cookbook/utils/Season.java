/*
 * Copyright (c) 2017 Jonathan Burk. All rights reserved.
 */
package com.jburk.cookbook.utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Represents a season of the year.
 * 
 * @author Jonathan Burk
 */
public class Season {  
  private static final Season SPRING = new Season("Spring", 3, 20);
  private static final Season SUMMER = new Season("Summer", 6, 21);
  private static final Season FALL = new Season("Fall", 9, 21);
  private static final Season WINTER = new Season("Winter", 12, 21);
  
  /**
   * List of season names
   */
  public static final List<String> NAMES = Arrays.asList(SPRING.name, SUMMER.name, FALL.name, WINTER.name);
  
  private final String name;
  private final int startingMonth;
  private final int startingDay;
  private Date startingDate;
  
  /**
   * Instantiates a new Season
   * @param name the name of the season
   * @param startingMonth the month the season begins
   * @param startingDay  the day the season begins
   */
  protected Season(String name, int startingMonth, int startingDay) {
    this.name = name;
    this.startingMonth = startingMonth;
    this.startingDay = startingDay;
  }
  
  /**
   * Creates a copy of the season for the specified year
   * @param year the year in which the season starts
   * @return  a copy of the season
   */
  protected Season createForYear(int year) {
    Season season = new Season(name, startingMonth, startingDay);
    
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, startingMonth, startingDay);
    
    season.startingDate = calendar.getTime();
    
    return season;
  }
  
  /**
   * Finds the season for a specified date
   * @param date the date determining the season
   * @return the season
   */
  public static Season findForYear(Date date) {
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    int lastYear = currentYear - 1;
    
    Season[] seasons = {
      WINTER.createForYear(lastYear),
      SPRING.createForYear(currentYear),
      SUMMER.createForYear(currentYear),
      FALL.createForYear(currentYear),
      WINTER.createForYear(currentYear)
    };
    
    for (int i = 1; i < seasons.length; i++) {
      if (date.before(seasons[i].startingDate)) {
        return seasons[i - 1];
      }
    }
    
    return seasons[seasons.length - 1];
  }
  
  /**
   * Gets the season name
   * @return the season name
   */
  public String getName() {
    return name;
  }
}
