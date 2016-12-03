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
 * This class sets the attributes and behaviors of the Spy, extending ActiveAgent which implements the
 * GamePiece interface. 
 */

public class Spy extends ActiveAgent{
	private static final long serialVersionUID = 1158821922633293763L;
	/**
	 * These fields describe how much ammo and how many lives the Spy has at the beginning of the game. 
	 * The invincibility is set to last 5 turns and is inactive when the spy is spawned. 
	 */
	private int bullet = 1;
	private int lives = 3;
	private int invincibleTurns = 5;
	private boolean invincibility = false;
	/**
	 * This method describes whether or not the spy has moved.
	 */
	private boolean moved = false;
	/**
	 * This method is a way of voiding the direction in which the spy is looking, which can later be activated 
	 * and chosen by the player.
	 */
	private char lookDirection = ' ';

	/**
	 * This method checks whether or not the spy has been moved.
	 */
	public void setMove(boolean m)
	{
		moved = m;
	}
	/**
	 * This method returns whether or not the spy has been moved. 
	 */
	public boolean getMove()
	{
		return moved;
	}
	/**
	 * This method is called when the invincibility power up is activated and it adds 5 invincible turns.
	 */
	public int getInvincibleTurns()
	{
		return invincibleTurns;
	}
	/**
	 * This method deducts one invincible turn for every turn the Spy takes after gaining invincibility.
	 */
	public void invincibleLoss()
	{
		invincibleTurns--;
	}
	/**
	 * This method returns the number of lives the Spy has left.
	 */
	public int getLives()
	{
		return lives;
	}
	/**
	 * This method deducts one life every time the Spy gets stabbed. 
	 */
	public int loseLife()
	{
		return lives -= 1;
	}
	/**
	 * This method resets the game, placing the Spy at the origin point with no invincibility, a bullet and
	 * three lives. 
	 */
	public void reset()
	{
		bullet = 1;
		lives = 3;
		invincibility = false;
		setLocation(0,0);
	}
	/**
	 * This activates the invincibility once the Spy picks up the power up. 
	 */
	public void activateInvincibility()
	{
		invincibility = true;
	}
	/**
	 * This disables invincibility once the 5 turns are depleted. 
	 */
	public void disableInvincibility()
	{
		invincibility = false;
	}
	/**
	 * This method returns whether or not the spy has the invincibility enabled or not.
	 */
	public boolean getInvincibility()
	{
		return invincibility;
	}
	
	/**
	 * This method expands on the image() method from the GamePiece interface. It sets the spot 
	 * on the map in which the spy is located to read 'S'.
	 */
	public char image(){
		return 'S';
	}
	/**
	 * This method returns how much ammo the Spy is carrying.
	 */
	public int getBullet()
	{
		return bullet;
	}
	/**
	 * This method adds another bullet to the Spy's ammo if the power up is activated. 
	 */
	public void addBullet()
	{
		bullet++;
	}
	/**
	 * This method removes a bullet from the Spy's ammo if he shoots. 
	 */
	public void useBullet(){
		bullet--;
	}
	/**
	 * This method returns the direction in which the spy is looking. 
	 */
	public char getLook()
	{
		return lookDirection;
	}
	/**
	 * This method sets the direction in which the player chooses to have the spy look. It is called on from
	 * the GameEngine and uses the player's input. 
	 */
	public void setLook(char direction)
	{
		lookDirection = direction;
	}
}
