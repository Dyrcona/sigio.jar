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

/**
 * Simulates a doubling die as used in backgammon and other games.
 *
 * <p>A doubling die works much like a regular die except that the
 * value of each face increases exponentially by a power of 2, rather
 * than linearly.</p>
 *
 * <p>Normally, only 6-faced, cube-shaped doublers are seen in the
 * wild. This class, however, allows you to create one with any number
 * of faces.</p>
 */

public class DoublingDie extends Die {

  private int m_maxSides;

  /**
   * Creates a new DoublingDie object with <code>s</code> number of
   * sides.
   * @param s the number of sides represented by this DoublingDie
   * object.
   */
  public DoublingDie(int s) {
    super(s);
    this.setMaxSides(s);
  }

  /**
   * Creates a DoublingDie instance with the default number of sides
   * (6).
   */
  public DoublingDie() {
    super(6);
    this.setMaxSides(6);
  }

  /**
   * {@inheritDoc}
   */
  public void setSides(int n) {
    super.setSides(n);
    this.setMaxSides(n);
  }
  
  /**
   * Simulates rolling a number of doubling dice with a number of
   * sides represented by this instance.
   *
   * @param n number of dice to roll.
   * @return an int holding the result of rolling the dice
   */ 
  public int roll(int n) {
    int result = 0;
    for (int i = 0; i < Math.abs(n); i++) {
      int p = super.roll(1);
      int r = 1;
      for (int j = 0; j < p; j++)
        r *= 2;
      result += r;
    }
    return result;
  }

  private void setMaxSides(int n) {
    m_maxSides = 1;
    for (int i = 0; i < n; i++)
      m_maxSides *= 2;
  }
}
