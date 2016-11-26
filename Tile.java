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
 * This class describes the pieces that can occupy any given place on the Map. 
 */

public class Tile{
	/**
	 * These constructors instantiate game pieces and power ups which can either be empty or hold
	 * an active agent or power up respectively. 
	 */
	private GamePiece gamepiece = new EmptyAA();
	private PowerUp powerup = new EmptyPU();
	/**
	 * These fields represent coordinates on the Map. 
	 */
	private int rowCoord, colCoord;
	
	/**
	 * This method sets a location on the Map for the Tile. 
	 */
	public Tile(int x, int y){
		rowCoord = x;
		colCoord = y;
	}
	/**
	 * This method prints the image of the power up if there is no Active Agent present on the tile. If there is an 
	 * active agent it prints its image to the map instead of the power up's.
	 */
	public char image(){
		if(gamepiece instanceof EmptyAA)
			return powerup.image();
		else
			return gamepiece.image();
	}
	/**
	 * This sets the location of the game piece on the map. 
	 */
	public void set(GamePiece g){
		gamepiece = g;
		gamepiece.setLocation(rowCoord,colCoord);
	}
	/**
	 * This method sets the location of the power up on the map. 
	 */
	public void set(PowerUp p){
		powerup = p;
	}
	/**
	 * This method returns the game piece as void of any Active Agents.
	 */
	public boolean noActiveAgent(){
		return gamepiece instanceof EmptyAA;
	}
	/**
	 * This method returns the power up as void of any power ups.
	 */
	public boolean noPowerUp(){
		return powerup instanceof EmptyPU;
	}
	/**
	 * This method returns the game piece as a room.
	 */
	public boolean isRoom(){
		return gamepiece instanceof Room;
	}
	/**
	 * This method returns the game piece as a spy.
	 */
	public boolean isSpy(){
		return gamepiece instanceof Spy;
	}
	/**
	 * This method returns the game piece as a ninja.
	 */
	public boolean isNinja(){
		return gamepiece instanceof Ninja;
	}
	/**
	 * This method returns the power up as not being empty.
	 */
	public boolean isPowerUp(){
		return !(powerup instanceof EmptyPU);
	}
	/**
	 * This method returns the game piece.
	 */
	public GamePiece getGamePiece(){
		return gamepiece;
	}
	/**
	 * This method returns the power up. 
	 */
	public PowerUp getPowerUp(){
		return powerup;
	}
}

