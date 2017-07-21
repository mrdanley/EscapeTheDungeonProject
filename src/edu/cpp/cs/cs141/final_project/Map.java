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
  				if(Math.abs(x-spy.getRowCoord())<=2 && y==spy.getColCoord())
  				{
  					if(x>spy.getRowCoord())
  					{
  						if(getAtLocation(spy.getRowCoord()+1,spy.getColCoord()) instanceof Room)
  							return ' ';
  						else
  							return tiles[x][y].image();
  					}
  					else
  					{
  						if(getAtLocation(x+1,spy.getColCoord()) instanceof Room)
  							return ' ';
  						else
  							return tiles[x][y].image();
  					}
  				}
  				else if(x==spy.getRowCoord() && Math.abs(y-spy.getColCoord())<=2)
  				{
  					if(y>spy.getColCoord())
  					{
  						if(getAtLocation(spy.getRowCoord(),spy.getColCoord()+1) instanceof Room)
  							return ' ';
  						else
  							return tiles[x][y].image();
  					}
  					else
  					{
  						if(getAtLocation(spy.getRowCoord(),y+1) instanceof Room)
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
  		if(!isValidLocation(x, y, false)){
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
	 * @param spy is the {@link Spy}
	 * @param direction is the Single character game directions (wasd)
	 * @return true if {@link Spy} has not moved, else return false
	 */
	public boolean moveSpy(Spy spy, char direction) {
		boolean invalidMove = movePiece(spy.getRowCoord(), spy.getColCoord(), direction);
		if(invalidMove)
			return false;
		else
		{
			spy.setMove(true);
			return true;
		}
	}	
	
	/**
	 * Will move an {@link ActiveAgent} in a valid direction by swapping
	 * the two {@link Tile}'s agents
	 * @param x Current location row
	 * @param y Current location column
	 * @param direction is the target destination in gamer directions (wasd)
	 * @return true if direction chosen is not valid, else return false
	 */
	public boolean movePiece(int x, int y, char direction) {
		GamePiece piece1 = getAtLocation(x, y);
		
		int[] delta = Map.intentDirection(direction);
		int targetX = x + delta[0];
		int targetY = y + delta[1];
		
		if (!isValidLocation(targetX, targetY, true)) {
			return true;
		}
		
		GamePiece piece2 = getAtLocation(targetX, targetY);
		set(targetX, targetY, piece1);
		set(x, y, piece2);
		return false;
	}
	/**
	 * This function will tell whether there is a {@link PowerUp}
	 * @param spy is the {@link Spy}
	 * @return true if there is a {@link PowerUp} at the {@link Spy}'s position, false otherwise
	 */
	public boolean powerUpCheck(Spy spy)
	{
		if(!tiles[spy.getRowCoord()][spy.getColCoord()].noPowerUp())
			return true;
		else
			return false;
	}
	/**
	 * This function returns a {@link PowerUp}
	 * @param spy is the {@link Spy}
	 * @return the {@link PowerUp} at the location
	 */
	public PowerUp getPowerUp(Spy spy)
	{
		return tiles[spy.getRowCoord()][spy.getColCoord()].getPowerUp();
	}
	
	/**
	 * Checks whether the desired location is on the grid. Also checks if space is occupied.
	 * @param x is the row location
	 * @param y is the column location
	 * @param agents If we are checking for agents
	 * @return true if the desired location is valid, false otherwise
	 */
	public boolean isValidLocation(int x, int y, boolean agents) {
		boolean isValid = true;
		
		//Out of bounds, then if tile is occupied
		if (x > tiles.length-1 || x < 0 || y > tiles.length-1 || y < 0) {
			isValid = false;
		} else if (agents && !noActiveAgent(x, y)) {
			isValid = false;
		}
		
		return isValid;
	}
	/**
	 * Removes the {@link PowerUp} at {@link Spy}'s location
	 * @param spy is the {@link Spy}
	 */
	public void removePowerUp(Spy spy)
	{
		tiles[spy.getRowCoord()][spy.getColCoord()].set(new EmptyPU());
	}
	
	public Object getPowerUpAtLocation(int x, int y) {
		return tiles[x][y].getPowerUp();
	}
	
	
	/**
	 * Generates a delta x,y coordinate given a direction. Think of it
	 * like a vector pointing in a direction
	 * Use:
	 * Assuming {@code x, y} are the original coords, get the coords in
	 * direction 'w'
	 * 
	 * {@code int[] delta = intentDirection('w');}
	 * {@code int targetX = x + delta[0];}
	 * {@code int targetY = y + delta[1];}
	 * 
	 * @param wasd Direction using traditional gamer cardinal directions
	 * @return an array containing an x delta and a y delta.
	 */
	public static int[] intentDirection(char wasd) {
		int[] delta = {0 , 0};
		switch (Character.toLowerCase(wasd)) {
		case 'w':
			delta[0]--;
			break;
		case 'a':
			delta[1]--;
			break;
		case 's':
			delta[0]++;
			break;
		case 'd':
			delta[1]++;
			break;
		}
		return delta;
	}
}
