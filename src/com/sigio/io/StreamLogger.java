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
package com.sigio.io;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * A class that can associate a particular java.io.InputStream with a
 * com.sigio.io.Logger. It is a subclass of java.lang.Thread so that it
 * can be used as a stream gobbler for a java.lang.Process.
 *
 * The run() method terminates when the InputStream is closed or if an
 * exception is thrown while reading from the stream or writing to the
 * logger.
 */

public class StreamLogger extends Thread {
  private InputStream m_stream;
  private Logger m_logger;
  private int m_which;

  /**
   * Construct a StreamLogger with an InputStream, a Logger, and a
   * log level.
   *
   * @param stream the input stream to read
   * @param logger the logger to write to
   * @param which the "log level" to use when logging
   * @throws IllegalArgumentException if which is &lt; 1 or &gt; 9
   */
  public StreamLogger(InputStream stream, Logger logger, int which) {
    if (which < 1 || which > 9)
      throw new IllegalArgumentException();
    m_stream = stream;
    m_logger = logger;
    m_which = which;
  }

  @Override
  public void run() {
    try {
      String input;
      BufferedReader br = new BufferedReader(new InputStreamReader(m_stream));
      while ((input = br.readLine()) != null) {
        if (m_logger != null)
          m_logger.log(m_which, input);
      }
    }
    catch (Exception e) {
    }
  }
}
