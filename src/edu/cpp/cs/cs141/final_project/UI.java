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

public class UI{
	private String[] IngameMenu = new String[5];
	
	public UI(){
		IngameMenu[0] = "\tD[u]ngeon Legend";
		IngameMenu[1] = "\t[C]ontrols";
		IngameMenu[2] = "\tD[e]bug Mode";
		IngameMenu[3] = "\tE[x]it Game";
		IngameMenu[4] = "\t[N]ormal mode";
	}
	
	public void displayIngameMenu(int i){
		System.out.print(IngameMenu[i]);
	}
	public void displayMenu(){
		System.out.print("1. Start Game\n"+
						 "2. Exit Game\n"+
						 "Input: ");
	}
	public void displayEnterDungeonMessage(){
		System.out.println("You have entered the dungeon!!!\n");
	}
	public void displaySpyControls(){
		System.out.println("[W]Up [S]Down [A]Left [D]Right [Q]Shoot\n");
	}
	public void displayEndGameMessage()
	{
		System.out.println("Goodbye.");
	}
}
