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

import java.util.Scanner;

/**
 * This class
 * @author
 */

public class UI{
	private String[] IngameMenu = new String[5];
	private Scanner kb = new Scanner(System.in);
	
	public UI(){
		IngameMenu[0] = "\tD[u]ngeon Legend";
		IngameMenu[1] = "\tSa[v]e Game";
		IngameMenu[2] = "\tDebug [M]ode";
		IngameMenu[3] = "\tE[x]it Game";
		IngameMenu[4] = "\tNormal [M]ode";
	}
	public int getIntInput(){
		String input;
		boolean correctInput;
		do{
			System.out.print("Input: ");
			input = kb.next();
			if(input.equals("1") || input.equals("2") || input.equals("3"))
				correctInput = true;
			else
			{
				System.out.println("Invalid input. Try again.\n");
				correctInput = false;
			}
		}while(!correctInput);
		return Integer.parseInt(input);
	}
	public int getCharInput(){
		System.out.print("Input: ");
		char input = kb.next().charAt(0);
		return input;
	}
	public void displayDungeon(Map map)
	{
		System.out.println("-DUNGEON-");
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
				System.out.print("[" + map.image(i,j) + "]");
			if(i>0 && i<IngameMenu.length)
			{
				if(i==2)
				{
					if(Map.isDebug())
						System.out.print(IngameMenu[i+1]);
					else
						System.out.print(IngameMenu[i-1]);
				}
				else
					System.out.print(IngameMenu[i-1]);
			}
			//REMOVE BEFORE SUBMIT
			if(i==5)
				System.out.print("\tEnter [R] to respawn dungeon (Will Remove before submit)");
			System.out.println();
		}
		displaySpyControls();
	}
	public void displayMenu(){
		System.out.print("1. New Game\n"+
						"2. Load Game\n"+
						"3. Exit Game\n");
	}
	public void displayEnterDungeonMessage(){
		System.out.println("You have entered the dungeon!!!\n");
	}
	private void displaySpyControls(){
		System.out.println("[W]Up [S]Down [A]Left [D]Right [Q]Shoot\n");
	}
	public void displayGameLegend(){
		System.out.println("S = Spy\nN = Ninja\nX = Room\nI = Invincibility\n"
							+"R = Radar\nB = Bullet\nC = Briefcase\n");
	}
	public boolean invalidInput(){
		System.out.println("Invalid input. Enter again: ");
		return false;
	}
	public void displayEndGameMessage(int endGameType)
	{
		switch(endGameType)
		{
			case 1:
				System.out.println("You have exited the game...Goodbye!\n");
				break;
			case 2:
				System.out.println("You found the briefcase and escaped! Congratulations!!!\n");
				break;
			case 3:
				System.out.println("You were destroyed by a Ninja...GAME OVER.\n");
			default:
				break;
		}
	}
	public void displayInvalidMove()
	{
		System.out.println("Invalid move. Try Again.\n");
	}
}
