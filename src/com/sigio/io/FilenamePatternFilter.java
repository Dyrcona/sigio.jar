/*
 * Copyright © 2011,2015 Jason J.A. Stephenson
 * 
 * This file is part of sigio.jar.
 * 
 * sigio.jar is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * sigio.jar is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with sigio.jar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sigio.io;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A FilenameFilter implementation that filters filenames that match
 * regular expression patterns stored in an internal list.
 */
public class FilenamePatternFilter implements FilenameFilter {

  private ArrayList<Pattern> m_patternList;

  /**
   * Construct a FilenamePatternFilter with an empty regular
   * expression pattern list.
   */
  public FilenamePatternFilter() {
    m_patternList = new ArrayList<Pattern>();
  }

  /**
   * Construct a FilenamePatternFilter with a collection of
   * pre-compiled regular expression patterns.
   *
   * @param collection collection of regular expression patterns
   */
  public FilenamePatternFilter(Collection<Pattern> collection) {
    m_patternList = new ArrayList<Pattern>(collection);
  }

  /**
   * Construct a FilenamePatternFilter with a single pre-compiled
   * regular expression pattern.
   *
   * @param pattern regular expression pattern
   */
  public FilenamePatternFilter(Pattern pattern) {
    m_patternList = new ArrayList<Pattern>();
    this.add(pattern);
  }

  /**
   * Construct a FilenamePatternFilter with an array of pre-compiled
   * regular expression patterns.
   *
   * @param patterns array of regular expression patterns
   */
  public FilenamePatternFilter(Pattern[] patterns) {
    m_patternList = new ArrayList<Pattern>();
    for (int i = 0; i < patterns.length; i++)
      this.add(patterns[i]);
  }

  /**
   * Construct a FilenamePatternFilter with a regular expression
   * pattern string.
   *
   * @param patternStr string with a regular expression pattern
   */
  public FilenamePatternFilter(String patternStr) {
    m_patternList = new ArrayList<Pattern>();
    this.add(Pattern.compile(patternStr));
  }

  /**
   * Construct a FilenamePatternFilter with a regular expression
   * pattern string, and an option mask for the regular expression
   * compiler.
   *
   * @param patternStr string with a regular expression pattern
   * @param reOpts int with the regular expression compiler options
   */
  public FilenamePatternFilter(String patternStr, int reOpts) {
    m_patternList = new ArrayList<Pattern>();
    this.add(Pattern.compile(patternStr, reOpts));
  }

  /**
   * Construct a FilenamePatternFilter with an array of regular
   * expression pattern strings.
   *
   * @param patternStrs array of regular expression pattern strings
   */
  public FilenamePatternFilter(String[] patternStrs) {
    m_patternList = new ArrayList<Pattern>();
    for (int i = 0; i < patternStrs.length; i++)
      this.add(Pattern.compile(patternStrs[i]));
  }

  /**
   * Construct a FilenamePatternFilter with an array of regular
   * expression pattern strings and an option mask for the regular
   * expression compiler.
   *
   * @param patternStrs array of regular expression pattern strings
   * @param reOpts int with the regular expression compiler options
   */
  public FilenamePatternFilter(String[] patternStrs, int reOpts) {
    m_patternList = new ArrayList<Pattern>();
    for (int i = 0; i < patternStrs.length; i++)
      this.add(Pattern.compile(patternStrs[i], reOpts));
  }

  /**
   * Construct a FilenamePatternFilter with an array of regular
   * expression pattern strings and an array of option masks for the
   * regular expression compiler.  There should be one option for each
   * pattern string.
   *
   * @param patternStrs array of regular expression pattern strings
   * @param reOpts array of regular expression compiler options
   */
  public FilenamePatternFilter(String[] patternStrs, int[] reOpts) {
    m_patternList = new ArrayList<Pattern>();
    for (int i = 0; i < patternStrs.length; i++)
      this.add(Pattern.compile(patternStrs[i], reOpts[i]));
  }

  /**
   * Add a pre-compiled regular expression pattern to the internal
   * list.
   *
   * @param pattern regular expression pattern to add
   */
  public void add(Pattern pattern) {
    if (!m_patternList.contains(pattern))
      m_patternList.add(pattern);
  }

  /**
   * Add an array of pre-compiled regular expression patterns to the
   * internal list.
   *
   * @param patterns array of regular expression patterns to add
   */
  public void add(Pattern[] patterns) {
    for (int i = 0; i < patterns.length; i++)
      this.add(patterns[i]);
  }

  /**
   * Add a regular expression pattern string to the internal list.
   *
   * @param patternStr regular expression string to add
   */
  public void add(String patternStr) {
    this.add(Pattern.compile(patternStr));
  }

  /**
   * Add a regular expression pattern string to the internal list
   * and compile it with an option mask.
   *
   * @param patternStr regular expression string to add
   * @param reOpts int with the regular expression compiler options
   */
  public void add(String patternStr, int reOpts) {
    this.add(Pattern.compile(patternStr, reOpts));
  }

  /**
   * Add an array of regular expression pattern strings to the
   * internal list.
   *
   * @param patternStrs array of regular expression strings to add
   */
  public void add(String[] patternStrs) {
    for (int i = 0; i < patternStrs.length; i++)
      this.add(patternStrs[i]);
  }

  /**
   * Add an array of regular expression pattern strings to the
   * internal list and compile them with an option mask.
   *
   * @param patternStrs array of regular expression strings to add
   * @param reOpts int with the regular expression compiler options
   */
  public void add(String[] patternStrs, int reOpts) {
    for (int i = 0; i < patternStrs.length; i++)
      this.add(patternStrs[i], reOpts);
  }

  /**
   * Add an array of regular expression pattern strings to the
   * internal list and compile them with an array of option masks, one
   * for each pattern string.
   *
   * @param patternStrs array of regular expression strings to add
   * @param reOpts array of regular expression compiler options
   */
  public void add(String[] patternStrs, int[] reOpts) {
    for (int i = 0; i < patternStrs.length; i++)
      this.add(patternStrs[i], reOpts[i]);
  }

  /**
   * Remove a pre-compiled regular expression pattern from the
   * internal list.
   *
   * @param pattern regular expression pattern to remove
   */
  public void remove(Pattern pattern) {
    while (m_patternList.contains(pattern))
      m_patternList.remove(pattern);
  }

  /**
   * Remove an array of pre-compiled regular expression patterns
   * from the internal list.
   *
   * @param patterns array of regular expression patterns to remove
   */
  public void remove(Pattern[] patterns) {
    for (int i = 0; i < patterns.length; i++)
      this.remove(patterns[i]);
  }

  /**
   * {@inheritDoc}
   *
   * @param dir the directory in which the file was found
   * @param name the name of the file to check
   * @return <code>true</code> if the filename matches one of the
   * regular expression patterns in the internal list;
   * <code>false</code> otherwise
   */
  public boolean accept(File dir, String name) {
    boolean isAcceptable = false;
    for (int i = 0; i < m_patternList.size(); i++) {
      Pattern pattern = m_patternList.get(i);
      Matcher matcher = pattern.matcher(name);
      if (matcher.matches()) {
        isAcceptable = true;
        break;
      }
    }
    return isAcceptable;
  }
}
