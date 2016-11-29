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
 * This class is the user interface. It shows the text messages from the game and also takes user input.
 */

public class UI{
	private String[] IngameMenu = new String[5];
	private Scanner kb = new Scanner(System.in);
	
	/**
	 * This method shows the texts on the right of the grid. When the user presses "u", 
	 * the screen shows the letter symbols of the grid. WHen the user presses "v",
	 * the game will be saved. The user can press "M" to change mode between normal mode 
	 * and the debug mode. 
	 */
	
	public UI(){
		IngameMenu[0] = "\tD[u]ngeon Legend";
		IngameMenu[1] = "\tSa[v]e Game";
		IngameMenu[2] = "\tDebug [M]ode";
		IngameMenu[3] = "\tE[x]it Game";
		IngameMenu[4] = "\tNormal [M]ode";
	}
	/**
	 * This function gets input for the start menu
	 * @return an integer
	 */
	public int getStartIntInput(){
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
	
	
	/**
	 * This method gets a character input
	 * @return returns a character
	 */
	public char getCharInput(){
		System.out.print("Input: ");
		char input = kb.next().charAt(0);
		return input;
	}
	
	/**
	 * This method draws the dungeon
	 * @param map is the {@link Map} object
	 * @param spy is the {@link Spy} object
	 */
	public void displayDungeon(Map map, Spy spy)
	{
		System.out.println("\n-DUNGEON-");
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
				System.out.print("[" + map.image(i,j,spy) + "]");
			if(i>0 && i<IngameMenu.length)
			{
				if(i==3)
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
			if(i==6)
			{
				System.out.print("\tLives: ");
				for(int k=0;k<spy.getLives();k++)
					System.out.print("<3 ");
			}
			if(i==7)
				System.out.print("\tAmmo: "+spy.getBullet());
			if(spy.getInvincibility())
			{
				if(i==8)
					System.out.print("\tInvincible for "+spy.getInvincibleTurns()+" turns");
			}
			System.out.println();
		}
		displaySpyControls();
		spy.setLook(' ');
	}
	/**
	 * This methods shows the choices of starting a new game, loading a game and exiting the game.
	 */
	public void displayMenu(){
		System.out.print("1. Start Game\n"+
						"2. Load Game\n"+
						"3. Exit Game\n");
	}
	/**
	 * This method prints the message when the user has entered the dungeon.
	 */
	public void displayEnterDungeonMessage(){
		System.out.println("You have entered the dungeon!!!\n");
	}
	/**
	 * This method shows the control of the spy movements.
	 */
	private void displaySpyControls(){
		System.out.println("[W]Up [S]Down [A]Left [D]Right [Q]Shoot [E]Look\n");
	}
	
	/**
	 * This method displays the letter symbols on the grid after the user enters "u".
	 */
	public void displayGameLegend(){
		System.out.println("S = Spy\nN = Ninja\nX = Room\nI = Invincibility\n"
							+"R = Radar\nB = Bullet\nC = Briefcase\n");
	}
	
	/**
	 * This method shows up when the user enters an invalid input.
	 */
	public void invalidInput(){
		System.out.println("Invalid input. Enter again: ");
	}
	
	/**
	 * This method is a message which shows up when the game is ended. 
	 * @param endGameType is an integer which decides which endgame message to display
	 */
	public void displayEndGameMessage(int endGameType)
	{
		switch(endGameType)
		{
			case 1:
				System.out.println("You have exited the game.\n");
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
	
	/**
	 * This method is a message of spy losing a life.
	 */
	public void displaySpyDieMessage()
	{
		System.out.println("You were stabbed and lost a life!");
		displayEnterAnyKey();
	}
	
	/**
	 * This method is a message of exiting the program.
	 */
	public void displayExitProgramMessage()
	{
		System.out.println("You have exited the program...Goodbye!\n");
	}
	/**
	 * This method is for telling the user that he/she has entered the invalid move.
	 */
	public void displayInvalidMove()
	{
		System.out.println("Invalid move. Try Again.\n");
	}
	
	/**
	 * This method requests a filename from the user, after displaying a list
	 * of possible files in the current directory.
	 * 
	 * @param listFiles
	 * @return
	 */
	public String getFilename(String[] listFiles) {
		if (listFiles.length > 1 || !listFiles[0].isEmpty()){
			System.out.println("\nExisting saves:\n");
			for (String file : listFiles) {
				System.out.println(file);
			}
		}
		System.out.println("\nPlease Enter a Filename or Enter 'C' to Cancel:  ");
		return kb.next();
	}
	
	/**
	 * This method shows the file loaded successfully and requests the user
	 * to start the game. 
	 */
	public void displayFileLoad() {
		System.out.println("\n\nGame Data Loaded...\n"
				+ "Select 'Start Game' to begin!\n");
	}
	
	/**
	 * This method informs the user that the filename provided does
	 * not point to a valid game save file.
	 */
	public void displayFileError() {
		System.out.println("Filename provided does not contain a valid save.\n"
				+ "Try again.");
	}

	/**
	 * This method shows the message of entering the room from a wrong direction. The user can only enter 
	 * the room from the north direction.
	 */
	public void displayInvalidRoomMove()
	{
		System.out.println("Cannot enter room from this direction.");
	}
	/**
	 * This method shows the message of the empty room.
	 */
	public void displayEmptyRoomMessage()
	{
		System.out.println("Empty room...look elsewhere...");
		displayEnterAnyKey();
	}
	
	/**
	 * The method shows the message that notices the user a ninja is being killed.
	 */
	public void displayNinjaDeathMessage()
	{
		System.out.println("You killed one ninja!");
	}
	/**
	 * The method shows that the bullet does not kill the ninja.
	 */
	public void displayShotAir()
	{
		System.out.println("You shot at nothing and wasted a bullet!");
	}
	/**
	 * The method shows up when the user tries to shoot with no bullets.
	 */
	public void displayNoBulletMessage()
	{
		System.out.println("No bullets to shoot.");
		displayEnterAnyKey();
	}
	/**
	 * This method shows the shooting direction that the user can choose.
	 * @return a character deciding direction
	 */
	public char displayShootMenu()
	{
		System.out.println("Choose the direction you want to shoot\n");
		System.out.println("[W]Up [S]Down [A]Left [D]Right\n");
		return getCharInput();
	}
	/**
	 * This method shows the message of entering any keys to continue.
	 */
	private void displayEnterAnyKey()
	{
		System.out.print("Enter any key to continue: ");
		if (kb.hasNextLine()) kb.nextLine();
		kb.nextLine();
	}
	/**
	 * This method displays the look menu.
	 * @return a character deciding direction
	 */
	public char displayLookMenu()
	{
		System.out.println("Choose the direction you want to look\n");
		System.out.println("[W]Up [S]Down [A]Left [D]Right\n");
		return getCharInput();
	}
	/**
	 * This method shows the direction that a {@link Ninja} is in.
	 * @param direction is the character that determines which direction the {@link Ninja} is in
	 */
	public void pathAlertMessage(char direction)
	{
		switch(direction)
		{
			case 'w': System.out.println("ALERT! Ninja in NORTH direction!"); break;
			case 'a': System.out.println("ALERT! Ninja in WEST direction!"); break;
			case 's': System.out.println("ALERT! Ninja in SOUTH direction!"); break;
			case 'd': System.out.println("ALERT! Ninja in EAST direction!"); break;		
		}
	}
	/**
	 * This method shows the message of path clear in a direction
	 * @param direction is the character that determines which direction is clear
	 */
	public void pathClearMessage(char direction)
	{
		switch(direction)
		{
			case 'w': System.out.println("Path CLEAR in NORTH direction!"); break;
			case 'a': System.out.println("Path CLEAR in WEST direction!"); break;
			case 's': System.out.println("Path CLEAR in SOUTH direction!"); break;
			case 'd': System.out.println("Path CLEAR in EAST direction!"); break;		
		}
	}
}
