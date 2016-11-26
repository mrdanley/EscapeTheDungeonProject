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
 * This class represents the ninja attributes.
 * @author Johnson Ton / Daniel Le
 */

import java.util.Random;

public class Ninja extends ActiveAgent{
	private Random roll = new Random();
	
	/**
	 * This field checks if the ninja is still alive. If the ninja dies by getting shot by spy, return false. Otherwise true.
	 */
	private boolean alive = true;
	
	/**
	 * This field shares a boolean to see if the current position of the {@link Spy} is in range of the {@link Ninja}
	 */
	private boolean ISeeSpyUp;
	/**
	 * This field shares a boolean to see if the current position of the {@link Spy} is in range of the {@link Ninja}
	 */
	private boolean ISeeSpyDown;
	/**
	 * This field shares a boolean to see if the current position of the {@link Spy} is in range of the {@link Ninja}
	 */
	private boolean ISeeSpyLeft;
	/**
	 * This field shares a boolean to see if the current position of the {@link Spy} is in range of the {@link Ninja}
	 */
	private boolean ISeeSpyRight;
	
	
	/**
	 * This method changes the {@link Ninja}  position by moving it 1 column left/right or 1 row up/down.
	 * It will then set his/her previous spot as {@link EmptyAA} which is empty spot on the {@link Map}.
	 * The logic behind this is that I roll a number that can only land 1 to 4. 
	 * If it lands on 1, {@link Ninja} will attempt to move left.
	 * If it lands on 2, {@link Ninja}  will attempt to move up.
	 * If it lands on 3, {@link Ninja}  will attempt to move right.
	 * If it lands on 4, {@link Ninja}  will attempt to move down.
	 * Once I get a random roll, I now check if this is legal.
	 * If the spot the {@link Ninja}  wants to move has either a room, a wall, another ninja, the spy there, or is off the grid,
	 * I will deem this move illegal and reroll.
	 * 
	 * HOW TO IMPLEMENT TO GAME ENGINE :
	 * for (int i = 0; i < 6; i++)
	 *    ninjas[i].move();
	 * 
	 * @param Grid is for the method to take the {@link Map} and alter the ninja's position in it based on a random roll.
	 */
	public void move(Map Grid) {
		boolean isWallHere = true;
		boolean isNinjaHere = true;
		boolean isSpyHere = true;
		boolean triedUp = false;
		boolean triedDown = false;
		boolean triedLeft = false;
		boolean triedRight = false;

		while ((isWallHere == true) || (isNinjaHere == true) || (isSpyHere == true))
		{
			int row = getRowCoord();
			int column = getColCoord();
			int RandomRoll = 1 + roll.nextInt(4);
			
			switch(RandomRoll)
			{
				case 1:
				{
					triedLeft = true;
					column--;
					break;
				}
				case 2:
				{
					triedUp = true;
					row--;
					break;
				}
				case 3:
				{
					triedRight = true;
					column++;
					break;
				}
				case 4:
				{
					triedDown = true;
					row++;
					break;
				}
			}
			
			isWallHere = isOutOfBound(RandomRoll);
			if (isWallHere == false) {	
				isSpyHere = Grid.isSpy(row, column);
				isNinjaHere = Grid.isNinja(row, column);
			}	
			if ((isSpyHere == false) && (isNinjaHere == false) && (isWallHere == false))
			{
				GamePiece emptyIt = new EmptyAA();
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(row, column, this);
				break;
			}
			
			if ((triedLeft == true) && (triedRight == true) && (triedUp == true) && (triedDown == true))
				break;
		}
		
	}
	
	/**
 	 * This move method will make the {@link Ninja} move towards {@link Spy} if he is in line of sight
 	 * This uses the ISeeTheSpy method to decide if {@link Spy} is in LoS and {@link Ninja} act accordingly depending on the results.
 	 * 
 	 * HOW TO IMPLEMENT IN GAME ENGINE EXAMPLE :
 	 * for (int i = 0; i < 6; i++) {
 	 * 		if (ninjas[i].iSeeASpy(map) == true)
 	 * 			ninjas[i].moveTowardsSpy(map);
 	 * 		else
 	 * 			ninjas[i].move(map);
 	 * }
 	 * 
 	 * @param Grid takes {@link Map} parameter to set {@link Ninja} position in another spot of the {@link Map}
 	 * 
 	 */
	public void moveTowardsSpy (Map Grid)
	{
		GamePiece emptyIt = new EmptyAA();
		if (ISeeSpyUp == true)
		{
			if (Grid.isSpy(getRowCoord() - 1, getColCoord()) == true)
				return;
			else
			{
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(getRowCoord() - 1, getColCoord(), this);
			}
		}
		if (ISeeSpyDown == true)
		{
			if (Grid.isSpy(getRowCoord() + 1, getColCoord()) == true)
				return;
			else
			{
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(getRowCoord() + 1, getColCoord(), this);
			}
		}
		if (ISeeSpyLeft == true)
		{
			if (Grid.isSpy(getRowCoord(), getColCoord() - 1) == true)
				return;
			else
			{
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(getRowCoord(), getColCoord() - 1, this);
			}
		}
		if (ISeeSpyRight == true)
		{
			if (Grid.isSpy(getRowCoord(), getColCoord() + 1) == true)
				return;
			else
			{
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(getRowCoord(), getColCoord() + 1, this);
			}
		}
	}
	
	/**
	 * This method checks if the {@link Spy} is in the {@link Ninja} line of sight.
	 * @param Grid is to check the map if there are any objects (rooms, other ninjas) blocking {@link Ninja} line of sight from spy.
	 * @param spy to track where {@link Spy} current coordinates.
	 * @return true if {@link Ninja} sees Spy in his line of sight. Otherwise return false.
	 */
	public boolean ISeeTheSpy (Map Grid, Spy spy)
	{
		int Spyrow = spy.getRowCoord();
		int Spycol = spy.getColCoord();
		int Ninjarow = getRowCoord();
		int Ninjacol = getColCoord();
		ISeeSpyUp = false;
		ISeeSpyDown = false;
		ISeeSpyLeft = false;
		ISeeSpyRight = false;
		
		if (Ninjarow == Spyrow)
		{
			if (Ninjacol == 9)
				ISeeSpyRight = false;
			else if (Ninjacol < Spycol)
			{
				for (int i = Ninjacol + 1; i <= Spycol - 1; i++) 
				{
					if ((Grid.isNinja(Ninjarow, i) == true) || (Grid.isRoom(Ninjarow, i) == true))
					{
						ISeeSpyRight = false;
						break;
					}
					else
						ISeeSpyRight = true;
				}
				if (Ninjacol == (Spycol - 1))
					ISeeSpyRight = true;
			}
			else if (Ninjacol == 0)
				ISeeSpyLeft = false;
			else if (Ninjacol > Spycol)
			{
				for (int i = Ninjacol - 1; i >= Spycol + 1; i--)
				{
					if ((Grid.isNinja(Ninjarow, i) == true) || (Grid.isRoom(Ninjarow, i) == true))
					{
						ISeeSpyLeft = false;
						break;
					}
					else
						ISeeSpyLeft = true;
				}
				if (Ninjacol == (Spycol + 1))
					ISeeSpyLeft = true;
			}
		}
		
		if (Ninjacol == Spycol)
		{
			if (Ninjarow == 9)
				ISeeSpyDown = false;
			else if (Ninjarow < Spyrow)
			{
				for (int i = Ninjarow + 1; i <= Spyrow - 1; i++)
				{
					if ((Grid.isNinja(i, Ninjacol) == true) || (Grid.isRoom(i, Ninjacol) == true))
					{
						ISeeSpyDown = false;
						break;
					}
					else
						ISeeSpyDown = true;
				}
				if (Ninjarow == (Spyrow - 1))
					ISeeSpyDown = true;
	
			}
			else if (Ninjarow == 0)
				ISeeSpyUp = false;
			else if (Ninjarow > Spyrow)
				for (int i = Ninjarow - 1; i >= Spyrow + 1; i--)
				{
					if ((Grid.isNinja(i, Ninjacol) == true) || (Grid.isRoom(i, Ninjacol) == true))
					{
						ISeeSpyUp = false;
						break;
					}
					else
						ISeeSpyUp = true;
				}
			if (Ninjarow == (Spyrow + 1))
				ISeeSpyUp = true;
		}
		
		if ((ISeeSpyUp == true) || (ISeeSpyDown == true) || (ISeeSpyLeft == true) || (ISeeSpyRight == true))
			return true;
		return false;
	}
	
	
	/**
	 * This private method checks if the next movement is out of bound. This is to prevent the {@link Ninja}  moving off the grid and possibly crashing the game.
	 * @param decisionWhereToMove takes the decision based on the random roll from 1 to 4 
	 * where 1 = left, 2 = up, 3 = right, 4 = down to see if this move is possible.
	 * 
	 * @return true if it is impossible movement. False if it is ok to move.
	 */
	private boolean isOutOfBound(int decisionWhereToMove) {
		int checkNewRow = getRowCoord();
		int checkNewColumn = getColCoord();
		if (decisionWhereToMove == 1) {
			checkNewColumn--;
			if (checkNewColumn < 0)
				return true;
		}
		if (decisionWhereToMove == 2) {
			checkNewRow--;
			if (checkNewRow < 0)
				return true;
		}
		if (decisionWhereToMove == 3) {
			checkNewColumn++;
			if (checkNewColumn > 8)
				return true;
		}
		if (decisionWhereToMove == 4) {
			checkNewRow++;
			if (checkNewRow > 8)
				return true;
		}
		if (((checkNewColumn == 1) && (checkNewRow == 1)) || ((checkNewColumn == 1) && (checkNewRow == 4)) || ((checkNewColumn == 1) && (checkNewRow == 7)) || ((checkNewColumn == 4) && (checkNewRow == 1)) || ((checkNewColumn == 4) && (checkNewRow == 4)) || ((checkNewColumn == 4) && (checkNewRow == 7)) || ((checkNewColumn == 7) && (checkNewRow == 1)) || ((checkNewColumn == 7) && (checkNewRow == 4)) || ((checkNewColumn == 7) && (checkNewRow == 7)))
			return true;
		return false;
	}
	
	/**
	 * This method returns the symbol used on the grid to represent ninja on the grid which is N for ninja.
	 */
	public char image(){
		return 'N'; 
	}
	
	/**
	 * This method checks if the ninja is still alive.
	 * @return true if alive. Otherwise false.
	 */
	public boolean isAlive()
	{
		return alive;
	}
	
	/**
	 * This method changes ninja from alive to dead.
	 * This should be used when the spy shoots the ninja.
	 */
	public void kill()
	{
		alive = false;
	}
}