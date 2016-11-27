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
 */
public class Map{
	/**
	 * This field shows the mode of the game.
	 */
	public static boolean debugMode = false;
	/**
	 * This field creates the {@link Tile} array of objects that {@link Map} will consist of
	 */
	private Tile[][] tiles;
	/**
	 * These fields represent the row and column location of {@link Spy}
	 */
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
	 * This function tells whether the game is in debug mode or not.
	 * @return value of {@link #debugMode}
	 */
	public static boolean isDebug(){
		return debugMode;
	}
  	/**
  	 * Puts a {@link GamePiece} on the grid, at the location specified by the x and y parameters.
  	 * @param x the desired row position
  	 * @param y the desired column position
  	 * @param g the {@link GamePiece} you want to set
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
  	 * @param x the desired row position
  	 * @param y the desired column position
  	 * @param p the {@link PowerUp} you want to set
  	 */
  	public void set(int x, int y, PowerUp p){
  		tiles[x][y].set(p);
  	}
  	
  	/**
  	 * Checks what gamepiece is at a specified location.
  	 * @param x the row position of the grid
  	 * @param y the column position of the grid
  	 * @return the {@link GamePiece} at the position you specify
  	 */
  	public GamePiece getAtLocation(int x, int y) {
  		return tiles[x][y].getGamePiece();
  	}
  	
	public int getLength() {
  		return tiles.length;
  	}
  	
  	public int getWidth() {
  		return tiles[0].length;
  	}
	
  	/**
  	 * This function returns the image (character) of the entity on a particular grid position.
  	 * @param x the row position of the grid
  	 * @param y the column position of the grid
  	 * @param spy is the {@link Spy} object
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
  	 * This function tells whether there is an {@link ActiveAgent} in the location
  	 * @param x is the row location
  	 * @param y is the column location
  	 * @return true if the specified tile holds no {@link ActiveAgent}, false otherwise
  	 */
  	public boolean noActiveAgent(int x, int y){
  		return tiles[x][y].noActiveAgent();
  	}
  	/**
  	 * This function tells whether there is an {@link PowerUp} in the location
  	 * @param x is the row location
  	 * @param y is the column location
  	 * @return true if the specified {@link Tile} holds no {@link PowerUp}, false otherwise
  	 */
  	public boolean noPowerUp(int x, int y){
  		return tiles[x][y].noPowerUp();
  	}
  	/**
  	 * This function tells whether there is an {@link Room} in the location
  	 * @param x is the row location
  	 * @param y is the column location
  	 * @return true if there is a {@link Room} at the specified {@link Tile}, false otherwise
  	 */
  	public boolean isRoom(int x, int y){
  		return tiles[x][y].isRoom();
  	}
  	/**
  	 * This function tells whether there is an {@link Spy} in the location
  	 * @param x is the row location
  	 * @param y is the column location
  	 * @return true if the {@link Spy} is at the specified {@link Tile}, false otherwise
  	 */
  	public boolean isSpy(int x, int y){
  		return tiles[x][y].isSpy();
  	}
  	/**
  	 * This function tells whether there is an {@link Ninja} in the location
  	 * @param x is the row location
  	 * @param y is the column location
  	 * @return true if there is a {@link Ninja} at the specified {@link Tile}, false otherwise
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
  	 * Clears the grid, replacing it with a new grid of {@link Tile}'s
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
	 * @param direction is the Single character game directions (wasd)
	 */
	public void moveSpy(char direction) {
		movePiece(spyX, spyY, direction);
	}	
	
	/**
	 * Will move an {@link ActiveAgent} in a valid direction by swapping
	 * the two {@link Tile}'s agents
	 * @param x Current location row
	 * @param y Current location column
	 * @param direction is the target destination in gamer directions (wasd)
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
	 * This function will tell whether there is a {@link PowerUp}
	 * @return true if there is a {@link PowerUp} at the {@link Spy}'s position, false otherwise
	 */
	public boolean powerUpCheck()
	{
		if(!tiles[spyX][spyY].noPowerUp())
			return true;
		else
			return false;
	}
	/**
	 * This function returns a {@link PowerUp}
	 * @return the {@link PowerUp} at the location
	 */
	public PowerUp getPowerUp()
	{
		return tiles[spyX][spyY].getPowerUp();
	}
	/**
	 * Checks whether the desired location is on the grid
	 * Function is specifically for spyMove() function
	 * @param x is the row location
	 * @param y is the column location
	 * @return true if the desired location is valid, false otherwise
	 */
	private boolean checkValidLocation(int x, int y) {
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
	 * Removes the {@link PowerUp} at {@link Spy}'s location
	 */
	public void removePowerUp()
	{
		tiles[spyX][spyY].set(new EmptyPU());
	}
	
	public Object getPowerUpAtLocation(int x, int y) {
		return tiles[x][y].getPowerUp();
	}
}
