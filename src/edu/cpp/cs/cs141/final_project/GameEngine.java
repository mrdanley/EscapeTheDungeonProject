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
 * This class represents the GameEngine for the dungeon game that runs the game and interacts with the {@link UI}
 *   as well as all the objects in the game. It also keeps track of the states of all the objects.
 * @author
 */

import java.util.*;
public class GameEngine{
	private Map map = new Map();
	private Ninja[] ninjas = new Ninja[6];
	private PowerUp[] powerups = new PowerUp[3];
	private Spy spy = new Spy();
	private Room[] rooms = new Room[9];
	private UI ui = new UI();
	
	private Scanner kb = new Scanner(System.in);
	private Random rand = new Random();
	private int rowSpawn, colSpawn;
	
	public void gameStart(){
		gameSet();
		ui.displayMenu();
		int startInput = kb.nextInt();
		do{
			switch(startInput){
				case 1:
				{
					displayDungeonDebug();//change to dark version once game finished
					break;
				}
				case 2:
					ui.displayEndGameMessage();
					break;
				default:
					break;
			}
		}while(startInput<1 || startInput>2);
	}
	private void setRooms()
	{
		for(int i=0;i<9;i++)
			rooms[i] = new Room();
		rooms[rand.nextInt(9)].setBriefcase();
		  
		map.set(1,1,rooms[0]);
		map.set(1,4,rooms[1]);
		map.set(1,7,rooms[2]);
		map.set(4,1,rooms[3]);
		map.set(4,4,rooms[4]);
		map.set(4,7,rooms[5]);
		map.set(7,1,rooms[6]);
		map.set(7,4,rooms[7]);
		map.set(7,7,rooms[8]);
	}
	private void setNinjas()
	{
		rowSpawn = rand.nextInt(9);
		colSpawn = rand.nextInt(9);
		boolean spawnNinja;
		for(int i=0;i<6;i++)
		{
			spawnNinja = false;
			ninjas[i] = new Ninja();
			do{
				if(map.noActiveAgent(rowSpawn,colSpawn))
				{
					map.set(rowSpawn, colSpawn, ninjas[i]);
					spawnNinja = true;
				}
				else
				{
					rowSpawn = rand.nextInt(9);
					colSpawn = rand.nextInt(9);
				}
			}while(!spawnNinja);
		}
	}
	private void setPowerUps()
	{
		powerups[0] = new Bullet();
		powerups[1] = new Invincibility();
		powerups[2] = new Radar();
		int powerup_num = 0;
		do{
			rowSpawn = rand.nextInt(9);
			colSpawn = rand.nextInt(9);
			if(map.noPowerUp(rowSpawn,colSpawn) && !map.isRoom(rowSpawn,colSpawn))
			{
				map.set(rowSpawn, colSpawn, powerups[powerup_num]);
				powerup_num++;
			}
		}while(powerup_num<3);
	}
	private void setSpy()
	{
		map.set(8,0,spy);
	}
	private void gameSet(){
		setRooms();
		setNinjas();
		setPowerUps();
		setSpy();
	}
	private void displayDungeonDebug(){
		System.out.println("-DUNGEON-");
		System.out.println("-Debug Ver.-");
		for(int i=0;i<9;i++)
		{
			//need to fix debug mode briefcase show
			for(int j=0;j<9;j++)
			{
				System.out.print("[" + map.image(i,j) + "]");
			}
			if(i>0 && i<5)
			{
				if(i==3)
					ui.displayIngameMenu(i+1);
				else
					ui.displayIngameMenu(i-1);
			}
			System.out.println();
		}
		ui.displaySpyControls();
	}
	/**
	 * still working on this function. only shows spy and rooms atm
	 */
	private void displayDungeonDark(){
		System.out.println("-DUNGEON-");
		System.out.println("-Night Goggles Ver.-");
		for(int i=0;i<9;i++)
		{
			for(int j=0;j<9;j++)
			{
				if(map.isRoom(i,j) || map.isSpy(i,j))
					System.out.print("[" + map.image(i,j) + "]");
				else
					System.out.print(' ');
			}
			if(i>0 && i<5)
				ui.displayIngameMenu(i-1);
			System.out.println();
		}
		ui.displaySpyControls();
	}
}
