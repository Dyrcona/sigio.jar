/*
 * Copyright © 2011 Merrimack Valley Library Consortium and
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
package com.sigio.io;
import java.io.OutputStream;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * A simple logging class for when log4j is overkill, but a simple
 * OutputStream is not enough.
 * 
 * <p>The class supports logging at several levels and an optional
 * debug level that can be toggled on or off at run time. The
 * following log levels are supported:</p>
 * <dl>
 * <dt>activity</dt><dd>use for program activity</dd>
 * <dt>debug</dt><dd>use for debug messages - can be turned on or off</dd>
 * <dt>error</dt><dd>use for error conditions</dd>
 * <dt>exception</dt><dd>used for logging exceptions</dd>
 * <dt>info</dt><dd>use for informational messages</dd>
 * <dt>internal</dt><dd>use for special, internal messages</dd>
 * <dt>warn</dt><dd>use for warnings of things that are not quite errors</dd>
 * </dl>
 */

public class Logger {

  private boolean m_debugMode = false;

  private OutputStream m_outputStream = null;

  private OutputStream m_activityStream = null;
  private OutputStream m_debugStream = null;
  private OutputStream m_errorStream = null;
  private OutputStream m_infoStream = null;
  private OutputStream m_internalStream = null;
  private OutputStream m_warnStream = null;

  private String m_applicationName = "Logger";

  private ResourceBundle m_resources = null;
  private SimpleDateFormat m_dateFormat = null;

  private String m_EOL = System.getProperty("line.separator");

  /** Use to log to standard output. */
  public static final int STDOUT = 1;
  /** Use to log to standard error. */
  public static final int STDERR = 2;
  /** Use for program activity. */
  public static final int ACTIVITY = 3;
  /** Use for debug messages. */
  public static final int DEBUG = 4;
  /** Use for error conditions. */
  public static final int ERROR = 5;
  /** Used for logging exceptions to the error log. */
  public static final int EXCEPTION = 6;
  /** Use for informational messages. */
  public static final int INFO = 7;
  /** Use for special, internal messages. */
  public static final int INTERNAL = 8;
  /** Use for warnings of things that are not quite errors. */
  public static final int WARN = 9;
  
  /**
   * Construct a Logger with a default OutputStream for it to write
   * to.
   *
   * @param out OutputStream to write to
   */
  public Logger(OutputStream out) {
    this.setOutputStream(out);
    this.loadResources();
  }

  /**
   * Construct a Logger with a default OutputStream and a default
   * application name to use in the log.
   *
   * @param out OutputStream to write to
   * @param appName string to use for the application name in the log
   */
  public Logger(OutputStream out, String appName) {
    this(out);
    this.setApplicationName(appName);
  }

  /**
   * Construct a Logger with a default OutputStream and a default
   * application name to use in the log.
   *
   * <p>The debugMode parameter can be used to turn on logging of
   * debug messages by passing in a boolean with a true value. The
   * default is for logging of debug messages to be off.</p>
   *
   * @param out OutputStream to write to
   * @param appName string to use for the application name in the log
   * @param debugMode boolean to indicate if you want to log debug
   * messages or not
   */
  public Logger(OutputStream out, String appName, boolean debugMode) {
    this(out, appName);
    this.setDebugMode(debugMode);
  }

  /**
   * Construct a Logger with a default OutputStream.
   *
   * <p>The debugMode parameter can be used to turn on logging of
   * debug messages by passing in a boolean with a true value. The
   * default is for logging of debug messages to be off.</p>
   *
   * @param out OutputStream to write to
   * @param debugMode boolean to indicate if you want to log debug
   * messages or not
   */
  public Logger(OutputStream out, boolean debugMode) {
    this(out);
    this.setDebugMode(debugMode);
  }

  /**
   * Change the application name used in the log after object
   * construction.
   *
   * @param appName string to use for the application name in the log
   */
  public synchronized void setApplicationName(String appName) {
    m_applicationName = appName;
  }

  /**
   * Get the application name used in the log file.
   *
   * @return string indicating the application name used in the log
   */
  public synchronized String getApplicationName() {
    return m_applicationName;
  }

  /**
   * Get the debug mode flag.
   */
  public synchronized boolean getDebugMode() {
    return m_debugMode;
  }

  /**
   * Change the debug mode flag at runtime.
   *
   * @param debugMode boolean to turn debug log on (if true) or off (if false)
   */
  public synchronized void setDebugMode(boolean debugMode) {
    m_debugMode = debugMode;
  }

  /**
   * Change the main OutputStream at runtime.
   *
   * @param out new outputstream to use for log
   */
  public synchronized void setOutputStream(OutputStream out) {
    m_outputStream = out;
  }

  /**
   * Set an alternate OutputStream to be used for activity log
   * messages. You can return this to the default log stream, by
   * setting it to null.
   *
   * @param out OutputStream for activity log messages
   */
  public synchronized void setActivityStream(OutputStream out) {
    m_activityStream = out;
  }

  /**
   * Set an alternate OutputStream to be used for debug log
   * messages. You can return this to the default log stream, by
   * setting it to null.
   *
   * @param out OutputStream for debug log messages
   */
  public synchronized void setDebugStream(OutputStream out) {
    m_debugStream = out;
  }

  /**
   * Set an alternate OutputStream to be used for error log
   * messages. You can return this to the default log stream, by
   * setting it to null.
   *
   * <p>The error stream is also used for exception logging.</p>
   *
   * @param out OutputStream for error log messages
   */
  public synchronized void setErrorStream(OutputStream out) {
    m_errorStream = out;
  }

  /**
   * Set an alternate OutputStream to be used for info log
   * messages. You can return this to the default log stream, by
   * setting it to null.
   *
   * @param out OutputStream for info log messages
   */
  public synchronized void setInfoStream(OutputStream out) {
    m_infoStream = out;
  }

  /**
   * Set an alternate OutputStream to be used for internal log
   * messages. You can return this to the default log stream, by
   * setting it to null.
   *
   * @param out OutputStream for internal log messages
   */
  public synchronized void setInternalStream(OutputStream out) {
    m_internalStream = out;
  }

  /**
   * Set an alternate OutputStream to be used for warn log
   * messages. You can return this to the default log stream, by
   * setting it to null.
   *
   * @param out OutputStream for warn log messages
   */
  public synchronized void setWarnStream(OutputStream out) {
    m_warnStream = out;
  }

  /**
   * Change the date format pattern for log timestamps if you don't
   * like the default.
   *
   * @param format string with the new date format pattern
   */
  public synchronized void setDateFormatPattern(String format) {
    m_dateFormat = new SimpleDateFormat(format);
  }

  /**
   *  Query the date format currently in use.
   *
   * @return string containing the current date format
   */
  public synchronized String getDateFormatPattern() {
    return m_dateFormat.toPattern();
  }

  /**
   * Print a formatted log message to stdout.
   *
   * @param string string message to log
   */
  public synchronized void logStdout(String string) {
    System.out.printf("%s %s: %s%s", this.getTimeStampStr(), this.getApplicationName(), string, m_EOL);
  }

  /**
   * Print a formatted log message to stderr.
   *
   * @param string string message to log
   */
  public synchronized void logStderr(String string) {
    System.err.printf("%s %s: %s%s", this.getTimeStampStr(), this.getApplicationName(), string, m_EOL);
  }

  /**
   * Print a formatted message to the activity log.
   *
   * @param string string message to log
   */
  public synchronized void logActivity(String string) {
    OutputStream out = (m_activityStream == null) ? m_outputStream : m_activityStream;
    this.streamWrite(out, m_resources.getString("activity"), string);
  }

  /**
   * Print a formatted message to the debug log.
   *
   * @param string string message to log
   */
  public synchronized void logDebug(String string) {
    if (m_debugMode) {
      OutputStream out = (m_debugStream == null) ? m_outputStream : m_debugStream;
      this.streamWrite(out, m_resources.getString("debug"), string);
    }
  }

  /**
   * Print a formatted message to the error log.
   *
   * @param string string message to log
   */
  public synchronized void logError(String string) {
    OutputStream out = (m_errorStream == null) ? m_outputStream : m_errorStream;
    this.streamWrite(out, m_resources.getString("error"), string);
  }

  /**
   * Print a formatted message to the info log.
   *
   * @param string string message to log
   */
  public synchronized void logInfo(String string) {
    OutputStream out = (m_infoStream == null) ? m_outputStream : m_infoStream;
    this.streamWrite(out, m_resources.getString("info"), string);
  }

  /**
   * Print a formatted message to the internal log.
   *
   * @param string string message to log
   */
  public synchronized void logInternal(String string) {
    OutputStream out = (m_internalStream == null) ? m_outputStream : m_internalStream;
    this.streamWrite(out, m_resources.getString("internal"), string);
  }

  /**
   * Print a formatted message to the warn log.
   *
   * @param string string message to log
   */
  public synchronized void logWarn(String string) {
    OutputStream out = (m_warnStream == null) ? m_outputStream : m_warnStream;
    this.streamWrite(out, m_resources.getString("warn"), string);
  }

  /**
   * Log an exception to the error log. Prints a stack trace for the
   * exception.
   *
   * @param thrown the exception to log
   */
  public synchronized void logException(Throwable thrown) {
    StackTraceElement[] elems = thrown.getStackTrace();
    String what = m_resources.getString("exception");
    OutputStream out = (m_errorStream == null) ? m_outputStream : m_errorStream;
    this.streamWrite(out, what, thrown.getClass().getName() + ": " + thrown.getMessage());
    for (int i = 0; i < elems.length; i++)
      this.streamWrite(out, what, elems[i].toString());

  }

  /**
   * Print a formatted message to a given log.
   * @param which the log to write to
   * @param msg the msg to write
   * @throws IllegalArgumentException if which is &lt; 1 or &gt; 9
   * @since r20
   */
  public synchronized void log(int which, String msg) {
    switch (which) {
    case STDOUT:
      this.logStdout(msg);
      break;
    case STDERR:
      this.logStderr(msg);
      break;
    case ACTIVITY:
      this.logActivity(msg);
      break;
    case DEBUG:
      this.logDebug(msg);
      break;
    case ERROR:
      this.logError(msg);
      break;
    case EXCEPTION:
      OutputStream s = (m_errorStream == null) ? m_outputStream
        : m_errorStream;
      this.streamWrite(s, m_resources.getString("exception"), msg);
      break;
    case INFO:
      this.logInfo(msg);
      break;
    case INTERNAL:
      this.logInternal(msg);
      break;
    case WARN:
      this.logWarn(msg);
      break;
    default:
      throw new IllegalArgumentException();
    }
  }

  private void streamWrite(OutputStream out, String what, String string) {
    try {
      String output = String.format("%s %s: %s %s%s", this.getTimeStampStr(), this.getApplicationName(), what, string, m_EOL);
      out.write(output.getBytes());
      out.flush();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadResources() {
    m_resources = ResourceBundle.getBundle("com.sigio.io.Logger");
    m_dateFormat = new SimpleDateFormat(m_resources.getString("dateformat"));
  }

  private String getTimeStampStr() {
    StringBuffer buf = new StringBuffer();
    m_dateFormat.format(new Date(), buf, new FieldPosition(SimpleDateFormat.DATE_FIELD));
    return buf.toString();
  }

}
