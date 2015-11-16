/*
 * Copyright © 2011 Jason J.A. Stephenson
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

/**
 * A FilenameFilter implementation that filters files based on a list
 * of file extensions.
 */
public class FilenameExtensionFilter implements FilenameFilter {

  private ArrayList<String> m_extensionList;

  /**
   * Construct a FilenameExtensionFilter with an empty extension
   * list.
   */
  public FilenameExtensionFilter() {
    m_extensionList = new ArrayList<String>();
  }

  /**
   * Construct a FilenameExtensionFilter with a collection of
   * extensions.
   *
   * @param collection collection of file extension strings 
   */
  public FilenameExtensionFilter(Collection<String> collection) {
    m_extensionList = new ArrayList<String>(collection);
  }

  /**
   * Construct a FilenamExtensionFilter with an array of extensions.
   *
   * @param extensions array of filename extension strings
   */
  public FilenameExtensionFilter(String[] extensions) {
    m_extensionList = new ArrayList<String>();
    for (int i = 0; i < extensions.length; i++)
      this.add(extensions[i]);
  }
  /**
   * Add an extension string to the internal list of extensions.
   *
   * @param extension string with the extension to add
   */
  public void add(String extension) {
    String alt = (extension.startsWith("."))  ? extension.substring(1) : "." + extension;
    if (!m_extensionList.contains(extension) && !m_extensionList.contains(alt))
      m_extensionList.add(extension);
  }

  /**
   * Add an array of extension strings to the internal list.
   *
   * @param extensions array of string extensions to add
   */
  public void add(String[] extensions) {
    for (int i = 0; i < extensions.length; i++)
      this.add(extensions[i]);
  }

  /**
   * Remove an extension from the internal list.
   *
   * @param extension the string extension to remove
   */
  public void remove(String extension) {
    String alt =  (extension.startsWith("."))  ? extension.substring(1) : "." + extension;
    while (m_extensionList.contains(alt))
      m_extensionList.remove(alt);
    while (m_extensionList.contains(extension))
      m_extensionList.remove(extension);
  }

  /**
   * Remove an array of extensions from the internal list.
   *
   * @param extensions array of string extensions to remove
   */ 
  public void remove(String[] extensions) {
    for (int i = 0; i < extensions.length; i++)
      this.remove(extensions[i]);
  }

  /**
   * {@inheritDoc}
   *
   * @param dir the directory in which the file was found
   * @param name the name of the file to check
   * @return <code>true</code> if the filename ends with one of the
   * extensions in the internal list; <code>false</code> otherwise
   */
  public boolean accept(File dir, String name) {
    boolean isAcceptable = false;
    for (int i = 0; i < m_extensionList.size(); i++) {
      String ext = m_extensionList.get(i);
      if (!ext.startsWith("."))
        ext = "." + ext;
      isAcceptable = name.endsWith(ext);
      if (isAcceptable)
        break;
    }
    return isAcceptable;
  }

}
