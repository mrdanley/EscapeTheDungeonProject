/**
 * CS 141: Intro to Programming and Problem Solving
 * Professor: Edwin Rodríguez
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

/**
 * This interface represents a GamePiece in the dungeon which can be a {@link Spy}, {@link Ninja}, or {@link Room}.
 *   GamePieces cannot occupy the same space as one another but can share a space with PowerUps.
 * GamePiece will be implemented by the classes ActiveAgent and Room.
 */

public interface GamePiece{
	/**
	 * This method will be altered in the classes implementing this interface to return a specific character, 
	 * unique to each power up, to mark the map.
	 */
	char image();
	/**
	 * This method will be altered to set the location on the map of whichever object is implementing it. 
	 */
	void setLocation(int x, int y);
}
