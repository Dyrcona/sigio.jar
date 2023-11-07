/*
 * Copyright © 2011, 2023 Merrimack Valley Library Consortium and
 * Jason Stephenson <jason@sigio.com>
 *
 * This file is part of sigio.jar.
 *
 * sigio.jar is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * sigio.jar is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with sigio.jar.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.sigio.sql;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.prefs.Preferences;

/**
 * Class to act as a factory for database connections. It reads named
 * database properties from files in a specified directory and uses
 * the properties to make JDBC connections to the specified database.
 *
 * <p>This needs to be fleshed out to describe the contents of the
 * properties files and to discuss the use of user preferences.</p>
 */
public class Connector {

  private String m_dbName;
  private HashMap<String,Properties> m_propMap;
  private File m_propsDirectory;

  /**
   * Create an empty Connector. It has no default database name set,
   * and uses either the properties directory set in preferences or
   * the current working directory.
   */
  public Connector() {
    m_propMap = new HashMap<String,Properties>();
    m_dbName = null;
    // Get the propsDirectory from preferences, if set:
    try {
      Preferences prefs = Preferences.userNodeForPackage(Class.forName("org.mvlc.db.Connector"));
      String path = prefs.get("propsDir", null);
      if (path != null) {
        m_propsDirectory = new File(path);
        if (!m_propsDirectory.exists() || !m_propsDirectory.isDirectory())
          m_propsDirectory = null;
      }
      else
        m_propsDirectory = null;
    }
    catch (ClassNotFoundException cnfe) {
      System.err.println("This absolutely should not happen!");
      cnfe.printStackTrace();
      System.exit(40);
    }
    catch (Exception e) {
      m_propsDirectory = null;
    }
  }

  /**
   * Construct a connector with a default database name set. It uses
   * either the properties directory set in preferences or the
   * current working directory.
   *
   * @param dbName string with the name of the default database
   */
  public Connector(String dbName) {
    this();
    if (dbName != null)
      this.setDbName(dbName);
  }

  /**
   * Construct a connector and specify a properties directory for
   * this instance. It has no default database name set.
   *
   * @param propsDir directory from which to load properties
   * @throws NullPointerException if propsDir is null
   * @throws IllegalArgumentException if propsDir does not exist or
   * is not a directory
   */
  public Connector(File propsDir) {
    this();
    if (propsDir != null && propsDir.exists() && propsDir.isDirectory())
      m_propsDirectory = propsDir;
    else if (propsDir == null)
      throw new NullPointerException();
    else
      throw new IllegalArgumentException();
  }

  /**
   * Construct a connector, specify a properties directory for this
   * instance, and specify a default database name.
   *
   * @param propsDir directory from which to load properties
   * @param dbName string with the name of the default database
   * @throws NullPointerException if propsDir is null
   * @throws IllegalArgumentException if propsDir does not exist or
   * is not a directory
   */
  public Connector(File propsDir, String dbName) {
    this(propsDir);
    if (dbName != null)
      this.setDbName(dbName);
  }

  /**
   * Get a connection to a named database.
   *
   * @param dbName string with the name of the database to connect to
   * @return connection to the database or null on failure
   * @throws NullPointerException if dbName is null
   * @throws ClassNotFoundException if it is unable to load the
   * JDBC driver for the database
   * @throws SQLException if a SQLExceptions occurs while loading
   * the database driver
   * @throws MissingResourceException if the db properties file fails to load
   */
  public Connection getConnection(String dbName) throws ClassNotFoundException, SQLException {
    Connection con = null;

    if (dbName == null) {
      NullPointerException ex = new NullPointerException("getConnection(String) called with null argument.");
      throw ex;
    }

    if (! m_propMap.containsKey(dbName))
      this.loadDbProperties(dbName);

    Properties dbProps = m_propMap.get(dbName);
    String dbDriver = dbProps.getProperty("driver");
    String dbURL = dbProps.getProperty("url");
    Class.forName(dbDriver);
    con = DriverManager.getConnection(dbURL, dbProps);

    return con;
  }

  /**
   * Get a connection to the default database.
   *
   * @return connection to the database or null on failure
   * @throws NullPointerException if no default database name is set
   * @throws ClassNotFoundException if it is unable to load the
   * JDBC driver for the database
   * @throws SQLException if a SQLExceptions occurs while loading
   * the database driver
   * @throws MissingResourceException if the db properties file fails to load
   */
  public Connection getConnection() throws ClassNotFoundException, SQLException{
    return this.getConnection(m_dbName);
  }

  /**
   * Set the default database name.
   *
   * @param dbName string with the name of the new default database
   * @throws NullPointerException if dbName is null
   */
  public void setDbName(String dbName) {
    if (dbName == null) {
      NullPointerException ex = new NullPointerException("setDbName(String) called with null argument.");
      throw ex;
    }
    this.loadDbProperties(dbName);
    m_dbName = dbName;
  }

  /**
   * Get the default database name.
   *
   * @return string with the default database name or null if not set
   */
  public String getDbName() {
    return m_dbName;
  }

  /**
   * Get the directory from which named properties files will be
   * loaded.
   */
  public File getPropsDirectory() {
    if (m_propsDirectory == null)
      m_propsDirectory = new File(System.getProperty("user.dir"));
    return m_propsDirectory;
  }

  /**
   * Set the directory from which named properties files will be
   * loaded.
   *
   * <p>This allows an instance to override the class-wide
   * preferences.</p>
   *
   * @param propsDir directory from which to load properties files
   * @throws NullPointerException if propsDir is null
   * @throws IllegalArgumentException if propsDir does not exist or
   * is not a directory
   */
  public void setPropsDirectory(File propsDir) {
    if (propsDir == null)
      throw new NullPointerException("setPropsDirectory(File) called with null argument.");
    else if (!propsDir.exists())
      throw new IllegalArgumentException("Path " + propsDir.getAbsolutePath() + " does not exist.");
    else if (!propsDir.isDirectory())
      throw new IllegalArgumentException("Path " + propsDir.getAbsolutePath() + " is not a directory.");
    else
      m_propsDirectory = propsDir;
  }

  /**
   * Preload the properties file for a named database.
   *
   * @param dbName string name of the database properties to load
   * @throws NullPointerException if dbName is null
   * @throws MissingResourceException if the db properties file
   * fails to load
   */
  protected void loadDbProperties(String dbName) {
    if (dbName == null) {
      NullPointerException ex = new NullPointerException("loadDbProperties(String) called with null argument.");
      throw ex;
    }
    if (! m_propMap.containsKey(dbName)) {
      try {
        // Check for null m_propsDirectory and set to current
        // working directory.
        if (m_propsDirectory == null)
          m_propsDirectory = new File(System.getProperty("user.dir"));
        String propsFilePath = m_propsDirectory.getAbsolutePath();
        if (!propsFilePath.endsWith(File.separator))
          propsFilePath += File.separator;
        propsFilePath += dbName + ".properties";
        FileInputStream propsFile = new FileInputStream(propsFilePath);
        Properties props = new Properties();
        props.load(propsFile);
        m_propMap.put(dbName, props);
        propsFile.close();
      }
      catch (Exception e) {
        MissingResourceException ex = new MissingResourceException("Failed to load properties for database " + dbName, "org.mvlc.db.Connector", "dbName");
        throw ex;
      }
    }
  }

  /**
   * Get the class wide preferences setting for the directory from
   * which properties files will be loaded.
   *
   * @return string with the path to the properties directory;
   * <code>null</code> if the preference is not set or an exception
   * occurs
   */
  public static String getPropsDirectoryPreference() {
    String dirPath = null;
    try {
      Preferences prefs = Preferences.userNodeForPackage(Class.forName("org.mvlc.db.Connector"));
      dirPath = prefs.get("propsDir", null);
    }
    catch (Exception e) {
      dirPath = null;
    }
    return dirPath;
  }

  /**
   * Set the class-wide preference setting for the directory from
   * which properties will be loaded.
   *
   * @param directoryPath string with the directory path
   * @throws NullPointerException if directoryPath is null
   * @throws SecurityException if a security manager is present and
   * it denies <code>RuntimePermission("preferences")</code>
   */
  public static void setPropsDirectoryPreference(String directoryPath) {
    if (directoryPath == null)
      throw new NullPointerException();
    try {
      Preferences prefs = Preferences.userNodeForPackage(Class.forName("org.mvlc.db.Connector"));
      prefs.put("propsDir", directoryPath);
    }
    catch (ClassNotFoundException cnfe) {
      System.err.println("This absolutely should not happen!");
      cnfe.printStackTrace();
      System.exit(40);
    }    
  }
}
