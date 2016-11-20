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

/**
 * This class represents the ninja attributes.
 * @author Johnson Ton / Daniel Le
 */

import java.util.Random;

public class Ninja extends ActiveAgent{
	/**
	 * This field shares a boolean to see if the current position of the {@link Spy} is in range of the {@link Ninja}
	 */
	private boolean iSeeSpyUp;
	/**
	 * This field shares a boolean to see if the current position of the {@link Spy} is in range of the {@link Ninja}
	 */
	private boolean iSeeSpyDown;
	/**
	 * This field shares a boolean to see if the current position of the {@link Spy} is in range of the {@link Ninja}
	 */
	private boolean iSeeSpyLeft;
	/**
	 * This field shares a boolean to see if the current position of the {@link Spy} is in range of the {@link Ninja}
	 */
	private boolean iSeeSpyRight;
	
	Random roll = new Random();
	
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
	 * HOW TO IMPLEMENT TO GAME ENGINE EXAMPLE :
	 * for (int i = 0; i < 6; i++)
	 *    ninjas[i].move(map);
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
			
			if (RandomRoll == 1)
			{
				triedLeft = true;
				column--;		
			}
			if (RandomRoll == 2)
			{
				triedUp = true;
				row--;
			}
			if (RandomRoll == 3)
			{
				triedRight = true;
				column++;
			}
			if (RandomRoll == 4)
			{
				triedDown = true;
				row++;
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
	 * This move method will make the {@link Ninja} move towards {@link Spy} if he is in range.
	 * This uses the ISeeTheSpy method to decide if {@link Spy} is in range and {@link Ninja} act accordingly depending on the results.
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
		if (iSeeSpyUp == true)
		{
			if (Grid.isSpy(getRowCoord() - 1, getColCoord()) == true)
				System.out.print("");
			else
			{
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(getRowCoord() - 1, getColCoord(), this);
			}
		}
		if (iSeeSpyDown == true)
		{
			if (Grid.isSpy(getRowCoord() + 1, getColCoord()) == true)
				System.out.print("");
			else
			{
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(getRowCoord() + 1, getColCoord(), this);
			}
		}
		if (iSeeSpyLeft == true)
		{
			if (Grid.isSpy(getRowCoord(), getColCoord() - 1) == true)
				System.out.print("");
			else
			{
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(getRowCoord(), getColCoord() - 1, this);
			}
		}
		if (iSeeSpyRight == true)
		{
			if (Grid.isSpy(getRowCoord(), getColCoord() + 1) == true)
				System.out.print("");
			else
			{
				Grid.set(getRowCoord(), getColCoord(), emptyIt);
				Grid.set(getRowCoord(), getColCoord() + 1, this);
			}
		}
	}
	
	/**
	 * This method checks if the {@link Ninja} sees the {@link Spy} in his range on his turn (2 squares up/down/left/right).
	 * If {@link Spy} is in range, boolean will return true. 
	 * @param Grid takes {@link Map} as parameter to check where the {@link Spy} is located on the map.
	 * @return true if {@link Spy} 2 squares (up/down/left/right) in range of {@link Ninja}. Otherwise return false.
	 */
	public boolean IseeTheSpy (Map Grid)
	{
		int row = getRowCoord();
		int column = getColCoord();
		int rowS1 = row - 1, rowS2 = row - 2, rowA1 = row + 1, rowA2 = row + 2;
		int colS1 = column - 1, colS2 = column - 2, colA1 = column + 1, colA2 = column + 2;
		iSeeSpyUp = false;
		iSeeSpyDown = false;
		iSeeSpyLeft = false;
		iSeeSpyRight = false;
		
		if (colS1 < 0)
			iSeeSpyLeft = false;
		else if (colS2 < 0)
		{
			if (Grid.isSpy(row, colS1) == true)
				iSeeSpyLeft = true;
			else
				iSeeSpyLeft = false;
		}
		else
		{
			if ((Grid.isSpy(row, colS2) == true) && (Grid.isRoom(row, colS1) == false) && (Grid.isNinja(row, colS1) == false))
				iSeeSpyLeft = true;
			else
				iSeeSpyLeft = false;
		}
		
		if (rowS1 < 0)
			iSeeSpyUp = false;
		else if (rowS2 < 0)
		{
			if (Grid.isSpy(rowS1, column) == true)
				iSeeSpyUp = true;
			else
				iSeeSpyUp = false;
		}
		else
		{
			if ((Grid.isSpy(rowS2, column) == true) && (Grid.isRoom(rowS1, column) == false) && (Grid.isNinja(rowS1, column) == false))
				iSeeSpyUp = true;
			else
				iSeeSpyUp = false;
		}
		
		if (colA1 > 8)
			iSeeSpyRight = false;
		else if (colA2 > 8)
		{
			if (Grid.isSpy(row, colA1) == true)
				iSeeSpyRight = true;
			else
				iSeeSpyRight = false;

		}
		else
		{
			if ((Grid.isSpy(row, colA2) == true) && (Grid.isRoom(row, colA1) == false) && (Grid.isNinja(row, colA1) == false))
				iSeeSpyRight = true;
			else
				iSeeSpyRight = false;
		}
		
		if (rowA1 > 8)
			iSeeSpyDown = false;
		else if (rowA2 > 8)
		{
			if (Grid.isSpy(rowA1, column) == true)
				iSeeSpyDown = true;
			else
				iSeeSpyDown = false;
		}
		else
		{
			if ((Grid.isSpy(rowA2, column) == true) && (Grid.isRoom(rowA1, column) == false) && (Grid.isNinja(rowA1, column) == false))
				iSeeSpyDown = true;
			else
				iSeeSpyDown = false;
		}
		
		if ((iSeeSpyUp == true) || (iSeeSpyDown == true) || (iSeeSpyLeft == true) || (iSeeSpyRight == true))
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
}
