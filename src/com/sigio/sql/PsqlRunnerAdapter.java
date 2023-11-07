/*
 * Copyright © 2011, 2023 Merrimack Valley Library Consortium
 * and Jason Stephenson <jason@sigio.com>
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
/**
 * Adapts a PsqlHandlerTask into a Runnable.
 */
public class PsqlRunnerAdapter implements Runnable {
  private PsqlHandlerTask m_called;
  public PsqlRunnerAdapter(PsqlHandlerTask called) {
    m_called = called;
  }
  public void run() {
    try {
      m_called.call();
    }
    catch (Exception e) {}
  }
}
