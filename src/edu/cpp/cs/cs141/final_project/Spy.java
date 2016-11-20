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

public class Spy extends ActiveAgent{
	private int bullet = 1;
	private int lives = 3;
	private boolean invincibility = false;
	
	public int getLives()
	{
		return lives;
	}
	public int loseLife()
	{
		return lives -= 1;
	}
	public void reset()
	{
		bullet = 1;
		lives = 3;
		invincibility = false;
		setLocation(0,0);
	}
	public void activateInvincibility()
	{
		invincibility = true;
	}
	public void disableInvincibility()
	{
		invincibility = false;
	}
	public boolean getInvincibility()
	{
		return invincibility;
	}
	public boolean look(){
		return true;
	}
	public char image(){
		 return 'S'; 
	}
	public int getBullet()
	{
		return bullet;
	}
	public void addBullet()
	{
		bullet++;
	}
}
