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
package com.sigio.games.dice;
import java.lang.Math;
import java.util.Random;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * This class simulates dice of various sizes for use in game
 * programming.
 *
 * @author Jason J.A. Stephenson
 * @version 1.0
 */
public class Die {
  
  /**
   * Holds the number of sides on an instance of this class.
   */
  private int m_sides;

  /**
   * A static pseudo-random number generator shared by all class
   * intances.
   */
  private static Random ms_r;
  
  /**
   * Creates a new Die object with <code>s</code> number of sides.
   * @param s the number of sides represented by this
   * Die object.
   */
  public Die(int s) {  m_sides = Math.abs(s); }
  
  /**
   * Creates a new Die object with the default number (6) of sides. This
   * can be used to simulate your ordinary cube dice.
   */
  public Die() { this(6); }
  
  /**
   * Returns the number of sides in a class instance.
   * @return the int value of the number of sides in this Die instance.
   */
  public int getSides() { return m_sides; }
  
  /**
   * Accessor to set the number of sides in a class instance.
   * @param s the new number of sides for the Die.
   */
  public void setSides(int s) { m_sides = Math.abs(s); }
  
  /**
   * Simulates rolling a number of dice with a number of sides
   * represented by this instance. The return value ranges from
   * <code>n</code> * 1 to <code>n</code> * the number of sides in
   * this Die instance.
   *
   * @param n number of dice to roll.
   * @return an int holding the result of rolling the dice
   */ 
  public int roll(int n) {
    int result = 0;
    for (int i = 0; i < Math.abs(n); i++)
      result += ms_r.nextInt(m_sides) + 1;
    return result;
  }

  /**
   * Simulates rolling a single die. The value ranges from 1 to the
   * number of sides on the Die, which could be determined by
   * calling <code>getSides().</code>
   *
   * @return an int holding the result of rolling one die
   */
  public int roll() { return this.roll(1); }

  public String toString() {
    try {
      String basename = this.getClass().getName();
      ResourceBundle bundle = ResourceBundle.getBundle(basename);
      String format = bundle.getString("shortStringFormat");
      return String.format(format, this.getSides());
    }
    catch (MissingResourceException e) {
      return String.format("D%1$d", this.getSides());
    }
  }

  static {
    ms_r = new Random();
    Date now = new Date();
    ms_r.setSeed(now.getTime());
    now = null;
  }
}
