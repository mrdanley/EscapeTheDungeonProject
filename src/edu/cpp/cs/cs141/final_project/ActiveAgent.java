/**
 * CS 141: Intro to Programming and Problem Solving
 * Professor: Edwin RodrÃ­guez
 *
 * Final Project
 *
 * This program is a text-based game which consists of a spy trapped in a dungeon grid of 9x9 squares.
 *  There are 9 rooms in the dungeon, one of which contain a Briefcase that contains the key to exit the dungeon.
 *  There are 6 Ninjas which randomly move around the dungeon that can kill the Spy if adjacent to the Spy.
 *  Also, there are 3 Power-ups which randomly spawn across the dungeon at the start of the game which can aid the
 *  Spy in finding the Briefcase, surviving the Ninjas, or killing the Ninjas.
 *
 * Team Wired
 *   Brandon Gastelo, Daniel Le, Shiying Li, Austin Morris, Anna Olshanskaya, Johnson Ton
 */

package edu.cpp.cs.cs141.final_project;

import java.io.Serializable;

/**
 * This is an abstract class that contains Active Agent attributes.
 * ActiveAgents' subclasses include Spy and Ninja.
 */

public abstract class ActiveAgent implements GamePiece, Serializable {
	/**
	 * This field holds the number where {@link ActiveAgent} is currently set position in a grid as an multiarray.
	 */
	private int rowCoord, colCoord;
	
	/**
	 * This method sets the location of the {@link ActiveAgent}
	 * @param x represents the row of the grid.
	 * @param y represents the column of the grid.
	 */
	public void setLocation(int x, int y)
	{
		rowCoord = x;
		colCoord = y;
	}
	
	/**
	 * This method returns the row coordinates of {@link ActiveAgent}
	 * @return row coordinates of the {@link ActiveAgent}
	 */
	public int getRowCoord(){
		return rowCoord;
	}
	
	/**
	 * This method returns the column coordinates of {@link ActiveAgent}
	 * @return column coordinates of the {@link ActiveAgent}
	 */
	public int getColCoord(){
		return colCoord;
	}
	
}
