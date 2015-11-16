/*
 * Copyright © 2011 Jason J.A. Stephenson
 * 
 * This file is part of sigio.jar.
 * 
 * sigio.jar is free software: you can redistribute it and/or modify it
 * under the terms of the Lesser GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * sigio.jar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with sigio.jar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sigio.json;
/**
 * Special class for our own JSON exceptions.
 */
public class JSONException extends Exception {
  /**
   * Constructs a new exception with <code>null</code> as its detail
   * message.
   */
  public JSONException() {
    super();
  }

  /**
   * Constructs a new exception with the specified detail message.
   */
  public JSONException(String message) {
    super(message);
  }

  /**
   * Constructs a new exception with the specified detail message
   * and cause.
   */
  public JSONException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new exception with the specified cause and a
   * detail message of <code>(cause==null ? null :
   * cause.toString())</code> (which typically contains the class
   * and detail message of cause).
   */
  public JSONException(Throwable cause) {
    super(cause);
  }

}
