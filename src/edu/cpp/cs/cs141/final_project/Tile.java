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

public class Tile{
	private GamePiece gamepiece = new EmptyAA();
	private PowerUp powerup = new EmptyPU();
	private int rowCoord, colCoord;
	
	public Tile(int x, int y){
		rowCoord = x;
		colCoord = y;
	}
	public char image(){
		//possibly change to: {return (gamepiece instanceof EmptyAA) ? powerup.image() : gamepiece.image();}
		if(gamepiece instanceof EmptyAA)
			return powerup.image();
		else
			return gamepiece.image();
	}
	public void set(GamePiece g){
		this.gamepiece = g;
		gamepiece.setLocation(rowCoord,colCoord);
	}
	public void set(PowerUp p){
		this.powerup = p;
		powerup.setLocation(rowCoord,colCoord);
	}
	public boolean noActiveAgent(){
		return gamepiece instanceof EmptyAA;
	}
	public boolean noPowerUp(){
		return powerup instanceof EmptyPU;
	}
	public boolean isRoom(){
		return gamepiece instanceof Room;
	}
	public boolean isSpy(){
		return gamepiece instanceof Spy;
	}
	public boolean isNinja(){
		return gamepiece instanceof Ninja;
	}
	public boolean isPowerUp(){
		return !(powerup instanceof EmptyPU);
	}
	public GamePiece getGamePiece(){
		return gamepiece;
	}
}

