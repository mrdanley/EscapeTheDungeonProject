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
 * This class implements the Game Piece Interface. It is responsible for setting the nine rooms on the map. 
 * It also keeps track of which room contains the briefcase.  
 */

public class Room implements GamePiece, java.io.Serializable {
	private static final long serialVersionUID = 7720232975188205845L;
	/**
	 * These fields signify whether or not there is a briefcase in a room and whether or not the 
	 * radar has been activated, as well as the coordinates of the room in the map array. 
	 */
	private boolean briefcase = false, radarBriefcase = false;
	private int rowCoord,colCoord;
	
	/**
	 * This method sets the location of each of the nine room objects at an (x,y) coordinate on the map. 
	 */
	public void setLocation(int x, int y){
		rowCoord = x;
		colCoord = y;
	}
	/**
	 * This method returns the column, or y coordinate of the room as set in the setLocation() method. 
	 */
	public int getColCoord(){
		return colCoord;
	}
	/**
	 * This method returns the row, or x coordinate of the room as set in the setLocation() method. 
	 */
	public int getRowCoord(){
		return rowCoord;
	}
	
	/**
	 * This method expands on the image() method from the GamePiece interface. If the map has been placed in debug mode
	 * or the radar power up has been activated, it shows which room the briefcase is in by showing it as a 'C' on the map.
	 * All the other rooms are marked with an 'X', as is the one with the suitcase if neither the radar nor debug mode are
	 * active. 
	 */
	public char image(){
		if(Map.debugMode && hasBriefcase())
			return 'C';
		else if(radarBriefcase)
		{
			radarBriefcase = false;
			return 'C';
		}
		else
			return 'X';
  	}
	/**
	 * This method activates the radar and has the image() method return a 'C' instead of an 'X' for 
	 * the room containing the briefcase for one turn. 
	 */
	public void radarActivate()
	{
		radarBriefcase = true;
	}
	/**
	 * This method is called from the GameEngine to set a briefcase into one of the rooms at random
	 * when the map is first generated. 
	 */
	public void setBriefcase(){
		briefcase = true;
	}
	/**
	 * This method is called from the GameEngine and checks if the room has a briefcase or not when 
	 * the spy tries to enter it from above. 
	 */
	public boolean hasBriefcase(){
		return briefcase;
	}
}
