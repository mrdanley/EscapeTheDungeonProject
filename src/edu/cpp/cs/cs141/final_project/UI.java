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
	private String[] IngameMenu = new String[4];
	private Scanner kb = new Scanner(System.in);
	
	public UI(){
		IngameMenu[0] = "\tD[u]ngeon Legend";
		IngameMenu[1] = "\tDebug [M]ode";
		IngameMenu[2] = "\tE[x]it Game";
		IngameMenu[3] = "\tNormal [M]ode";
	}
	public int getIntInput(){
		System.out.print("Input: ");
		int input = kb.nextInt();
		return input;
	}
	public int getCharInput(){
		System.out.print("Input: ");
		char input = kb.next().charAt(0);
		return input;
	}
	@SuppressWarnings("static-access")
	public void displayDungeon(Map map)
	{
		System.out.println("-DUNGEON-");
		for(int i=0;i<map.getWidth();i++)
		{
			for(int j=0;j<map.getLength();j++)
				System.out.print("[" + map.image(i,j) + "]");
			if(i>0 && i<4)
			{
				if(i==2)
				{
					if(map.isDebug())
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
		System.out.print("1. Start Game\n"+
						 "2. Exit Game\n");
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
	public void displayEndGameMessage()
	{
		System.out.println("Goodbye.");
	}
}
