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
 * This class
 * @author
 */

import java.lang.Math;
public class Map{
	public static boolean debugMode = false;
	public boolean looking = false;
	private Tile[][] tiles;
	private int spyX, spyY;
	
	public Map(){
		tiles = new Tile[9][9];
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
				tiles[i][j] = new Tile(i,j);
		}
	}
	public static boolean isDebug(){
		return debugMode;
	}
  	public void set(int x, int y, GamePiece g){
  		if(g instanceof Spy)
  		{
  			spyX = x;
  			spyY = y;
  		}
  		tiles[x][y].set(g);
  	}
  	public void set(int x, int y, PowerUp p){
  		tiles[x][y].set(p);
  	}
  	
  	public GamePiece getAtLocation(int x, int y) {
  		return tiles[x][y].getGamePiece();
  	}
  	public boolean spyLooking(){
  		looking = true;
  		return looking;
  	}
  		
  	public char image(int x, int y){
  		if(!debugMode)
  		{
  			if(tiles[x][y].isNinja() || tiles[x][y].isPowerUp())
  			{
  				//creates default visibility square around spy
  				if((Math.abs(x-spyX)<=2 && y==spyY) || (x==spyX && Math.abs(y-spyY)<=2))
  					return tiles[x][y].image();
  				//if activated allows spy to look up and down the column and row in which he stands
  				if(looking == true){
  					
  						if((x==spyX && y>=0 && y<9) || (y==spyY && x>=0 && x<9)){
  							looking = false;
  							return tiles[x][y].image();	
  						}
  						else{
  							return' ';
  						}
  				}
  				else{
  					return ' ';
  				}
  				
  			}
  		}
  		return tiles[x][y].image();
  	}
  	
  	public boolean noActiveAgent(int x, int y){
  		return tiles[x][y].noActiveAgent();
  	}
  	public boolean noPowerUp(int x, int y){
  		return tiles[x][y].noPowerUp();
  	}
  	public boolean isRoom(int x, int y){
  		return tiles[x][y].isRoom();
  	}
  	public boolean isSpy(int x, int y){
  		return tiles[x][y].isSpy();
  	}
  	public boolean isNinja(int x, int y){
  		//Make sure the location is in the grid
  		if(x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length){
  			return false;
  		}
  		else{
  			return tiles[x][y].isNinja();
  		}
  	}
  	public void clear(){
  		tiles = new Tile[9][9];
  	}
	public void toggleMode() {
		debugMode = !debugMode;
	}
	
	/**
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
	public boolean powerUpCheck()
	{
		if(!tiles[spyX][spyY].noPowerUp())
			return true;
		else
			return false;
	}
	public PowerUp getPowerUp()
	{
		return tiles[spyX][spyY].getPowerUp();
	}
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
	public void removePowerUp()
	{
		tiles[spyX][spyY].set(new EmptyPU());
	}
}