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
package com.sigio.sql;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * FileFilter implementation to check for JDBC database properties
 * files. To be considered a JDBC database properties file, the file
 * be a valid, parseable properties file, and it must contain
 * <code>driver</code> and <code>url</code> keys. Optionally, the user
 * can specify a list of file extensions that the file must match in
 * order to be considered.
 */
public class DbPropertiesFileFilter implements FileFilter {

  private ArrayList<String> m_extensionList;

  /**
   * Construct a DbPropertiesFileFilter without an extensions list.
   */
  public DbPropertiesFileFilter() {
    m_extensionList = null;
  }

  /**
   * Construct a DbPropertiesFileFilter with a list of extensions
   * that the filename must have in order to be considered a valid
   * properties file. The filename need matchonly one extension from
   * the list.
   *
   *@param extensions array of strings to match against the filename
   */
  public DbPropertiesFileFilter(String[] extensions) {
    m_extensionList = new ArrayList<String>();
    for (int i = 0; i < extensions.length; i++)
      m_extensionList.add(extensions[i]);
  }

  /**
   * Construct a DbPropertiesFileFilter with a list of extensions
   * that the filename must have in order to be considered a valid
   * properties file. The filename need matchonly one extension from
   * the list.
   *
   *@param extensions collection of strings to match against the
   *filename
   */
  public DbPropertiesFileFilter(Collection<String> extensions) {
    m_extensionList = new ArrayList<String>(extensions);
  }

  /**
   * Tests whether or not the specified abstract pathname should be
   * included in a pathname list.
   *
   * @param pathname the abstract pathname to be tested
   * @return <code>true</code> if the file appears to be a valid
   * JDBC properties file; <code>false</code> otherwise
   */
  public boolean accept(File pathname) {
    boolean isAcceptable = false;
    if (pathname.exists() && pathname.isFile() && pathname.canRead()) {
      try {

        if (m_extensionList != null)
          if (!checkFilenameExtension(pathname.getName()))
            return isAcceptable;

        FileInputStream in = new FileInputStream(pathname);
        Properties p = new Properties();
        p.load(in);
        in.close();
        if (p.containsKey("driver") && p.containsKey("url"))
          isAcceptable = true;
      }
      catch (IOException e) {
        isAcceptable = false;
      }
    }
    return isAcceptable;
  }

  private boolean checkFilenameExtension(String filename) {
    boolean isAcceptable = false;
    for (int i = 0; i < m_extensionList.size(); i++) {
      String ext = m_extensionList.get(i);
      if (!ext.startsWith("."))
        ext = "." + ext;
      isAcceptable = filename.endsWith(ext);
      if (isAcceptable)
        break;
    }
    return isAcceptable;
  }

}
