/*
 * Copyright © 2011, 2023 Merrimack Valley Library Consortium and
 * Jason Stephenson <jason@sigio.com>
 *
 * This file is part of sigio.jar.
 *
 * sigio.jar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sigio.jar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with sigio.jar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sigio.sql;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.Map;
import com.sigio.io.Logger;
import com.sigio.io.StreamLogger;

public class PsqlHandlerTask implements Callable<Integer> {

  private ProcessBuilder m_builder = null;
  private Logger m_logger = null;

  /**
   * Construct a PsqlHandlerTask.
   *
   * @param sqlFile file that contains the sql commands to execute
   * @param directory the working directory to use when running the commands. Pass <code>null</code> to use the current working directory from the OS.
   * @param environment map of name=value pairs to use as the runtime environment. Pass <code>null</code> to use the default.
   * @param logger logger instance to use for logging psql output. Pass <code>null</code> to send output to stderr and stdout.
   */
  public PsqlHandlerTask(File sqlFile, File directory, Map<String, String> environment, Logger logger) {
    m_builder = new ProcessBuilder("psql", "-f", sqlFile.getAbsolutePath());
    if (directory != null)
      m_builder.directory(directory);
    if (environment != null) {
      Map<String, String> env = m_builder.environment();
      env.clear();
      env.putAll(environment);
    }
    m_logger = logger;
  }

  /**
   * Construct a PsqlHandlerTask using caller's working directory and environment.
   *
   * @param sqlFile file that contains the sql commands to execute
   * @param logger logger instance to use for logging psql output. Pass <code>null</code> to send output to stderr and stdout.
   */
  public PsqlHandlerTask(File sqlFile, Logger logger) {
    this(sqlFile, null, null, logger);
  }

  @Override
  public Integer call() throws Exception {
    int result = -1;
    Process p = m_builder.start();
    StreamLogger stdout = new StreamLogger(p.getInputStream(), m_logger, Logger.DEBUG);
    StreamLogger stderr = new StreamLogger(p.getErrorStream(), m_logger, Logger.ERROR);
    try {
      stdout.start();
      stderr.start();
      result = p.waitFor();
    }
    catch (Exception e) {
      if (m_logger != null)
        m_logger.logException(e);
      else
        throw e;
    }
    finally {
      Thread.currentThread().interrupted();
      try {
        p.getOutputStream().close();
        p.getInputStream().close();
        p.getErrorStream().close();
        p.destroy();
      }
      catch (Exception e) {
        if (m_logger != null)
          m_logger.logException(e);
        else
          throw e;
      }
    }
    return new Integer(result);
  }

}
