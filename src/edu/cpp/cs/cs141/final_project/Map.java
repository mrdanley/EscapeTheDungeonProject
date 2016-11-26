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

import java.lang.Math;
/**
 * This class represents the game map, which holds the grid of tiles.
 *
 */
public class Map{
	public static boolean debugMode = false;
	public boolean looking = false;
	private Tile[][] tiles;
	private int spyX, spyY;
	
	/**
	 * Instantiates the 9x9 grid of tiles.
	 */
	public Map(){
		tiles = new Tile[9][9];
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
				tiles[i][j] = new Tile(i,j);
		}
	}
	/**
	 * @return true if debug mode is turned on, false if otherwise.
	 */
	public static boolean isDebug(){
		return debugMode;
	}
  	/**
  	 * Puts a GamePiece on the grid, at the location specified by the x and y parameters.
  	 * @param x the desired x position
  	 * @param y the desired y position
  	 * @param g the gamepiece you want to set
  	 */
  	public void set(int x, int y, GamePiece g){
  		if(g instanceof Spy)
  		{
  			spyX = x;
  			spyY = y;
  		}
  		tiles[x][y].set(g);
  	}
  	/**
  	 * Puts a PowerUp on the grid, at the location specified by the x and y parameters.
  	 * @param x the desired x position
  	 * @param y the desired y position
  	 * @param p the powerup you want to set
  	 */
  	public void set(int x, int y, PowerUp p){
  		tiles[x][y].set(p);
  	}
  	
  	/**
  	 * Checks what gamepiece is at a specified location.
  	 * @param x the x position to check
  	 * @param y the y position to check
  	 * @return the GamePiece at the position you specify
  	 */
  	public GamePiece getAtLocation(int x, int y) {
  		return tiles[x][y].getGamePiece();
  	}
  	
  	/**
  	 * Returns the image (character) of the entity on a particular grid position.
  	 * @param x the x position of the grid
  	 * @param y the y position of the grid
  	 * @param spy
  	 * @return the image of the entity at the desired grid position.
  	 */
  	public char image(int x, int y, Spy spy){
  		if(!debugMode)
  		{
  			if(tiles[x][y].isNinja() || tiles[x][y].isPowerUp())
  			{
  				//if room is between spy and ninja or powerup, spy cannot see
  				if(Math.abs(x-spyX)<=2 && y==spyY)
  				{
  					if(x>spyX)
  					{
  						if(getAtLocation(spyX+1,spyY) instanceof Room)
  							return ' ';
  						else
  							return tiles[x][y].image();
  					}
  					else
  					{
  						if(getAtLocation(x+1,spyY) instanceof Room)
  							return ' ';
  						else
  							return tiles[x][y].image();
  					}
  				}
  				else if(x==spyX && Math.abs(y-spyY)<=2)
  				{
  					if(y>spyY)
  					{
  						if(getAtLocation(spyX,spyY+1) instanceof Room)
  							return ' ';
  						else
  							return tiles[x][y].image();
  					}
  					else
  					{
  						if(getAtLocation(spyX,y+1) instanceof Room)
  							return ' ';
  						else
  							return tiles[x][y].image();
  					}
  				}
  				else
  					return ' ';
  			}
  		}
  		return tiles[x][y].image();
  	}
  	/**
  	 * @param x
  	 * @param y
  	 * @return true if the specified tile holds no active agent, false otherwise
  	 */
  	public boolean noActiveAgent(int x, int y){
  		return tiles[x][y].noActiveAgent();
  	}
  	/**
  	 * @param x
  	 * @param y
  	 * @return true if the specified tile holds no powerup, false otherwise
  	 */
  	public boolean noPowerUp(int x, int y){
  		return tiles[x][y].noPowerUp();
  	}
  	/**
  	 * @param x
  	 * @param y
  	 * @return true if there is a room at the specified tile, false otherwise
  	 */
  	public boolean isRoom(int x, int y){
  		return tiles[x][y].isRoom();
  	}
  	/**
  	 * @param x
  	 * @param y
  	 * @return true if the spy is at the specified tile, false otherwise
  	 */
  	public boolean isSpy(int x, int y){
  		return tiles[x][y].isSpy();
  	}
  	/**
  	 * 
  	 * @param x
  	 * @param y
  	 * @return true if there is a ninja at the specified tile, false otherwise
  	 */
  	public boolean isNinja(int x, int y){
  		//Make sure the location is in the grid
  		if(x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length){
  			return false;
  		}
  		else{
  			return tiles[x][y].isNinja();
  		}
  	}
  	/**
  	 * Clears the grid, replacing it with a new grid of tiles
  	 */
  	public void clear(){
  		tiles = new Tile[9][9];
  	}
	/**
	 * Toggles debug mode on and off.
	 */
	public void toggleMode() {
		debugMode = !debugMode;
	}
	
	/**
	 * Moves the spy in the specified direction
	 * @param direction Single character gamer directions (wasd)
	 */
	public void moveSpy(char direction) {
		movePiece(spyX, spyY, direction);
	}	
	
	/**
	 * Will move an {@link ActiveAgent} in a valid direction by swapping
	 * the two {@link Tile}'s agents
	 * 
	 * @param x Current location x
	 * @param y Current location y
	 * @param direction target destination in gamer directions (wasd)
	 */
	public void movePiece(int x, int y, char direction) {
		GamePiece piece1 = getAtLocation(x, y);
		int targetX = x;
		int targetY = y;

		switch (direction) {
		case 'w':
			targetX--; //right
			break;
		case 'a':
			targetY--; //up
			break;
		case 's':
			targetX++; //down
			break;
		case 'd':
			targetY++; //left
			break;
		default:
			new Exception("Invalid directional input").printStackTrace();
			return;
		}
		
		if (checkValidLocation(targetX, targetY)) {
			return;
		}
		
		GamePiece piece2 = getAtLocation(targetX, targetY);
		set(targetX, targetY, piece1);
		set(x, y, piece2);
	}
	/**
	 * @return true if there is a powerup at the spy's position, false otherwise
	 */
	public boolean powerUpCheck()
	{
		if(!tiles[spyX][spyY].noPowerUp())
			return true;
		else
			return false;
	}
	/**
	 * @return a reference to the powerup at the spy's location
	 */
	public PowerUp getPowerUp()
	{
		return tiles[spyX][spyY].getPowerUp();
	}
	/**
	 * Checks whether the desired location is on the grid
	 * @param x
	 * @param y
	 * @return true if the desired location is valid, false otherwise
	 */
	public boolean checkValidLocation(int x, int y) {
		boolean isValid = true;
		
		if (x > tiles.length-1 || x < 0 || y > tiles.length-1 || y < 0) {
			isValid = false;
		}
		
		if (noActiveAgent(x, y)) {
			isValid = false;
		}
		
		return isValid;
	}
	/**
	 * Removes the powerup that the spy is standing on
	 */
	public void removePowerUp()
	{
		tiles[spyX][spyY].set(new EmptyPU());
	}
}