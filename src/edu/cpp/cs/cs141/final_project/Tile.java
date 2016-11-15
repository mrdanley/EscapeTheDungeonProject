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
	private GamePiece g = new EmptyAA();
	private PowerUp p = new EmptyPU();
	
	public char image(){
		if(g instanceof EmptyAA)
			return p.image();
		else
			return g.image();
	}
	public void set(GamePiece g){
		this.g = g;
	}
	public void set(PowerUp p){
		this.p = p;
	}
	public boolean noActiveAgent(){
		if(g instanceof EmptyAA)
			return true;
		else
			return false;
	}
	public boolean noPowerUp(){
		if(p instanceof EmptyPU)
			return true;
		else
			return false;
	}
	public boolean isRoom(){
		if(g instanceof Room)
			return true;
		else
			return false;
	}
	public boolean isSpy(){
		if(g instanceof Spy)
			return true;
		else
			return false;
	}
	public GamePiece getGamePiece(){
		return g;
	}
}

