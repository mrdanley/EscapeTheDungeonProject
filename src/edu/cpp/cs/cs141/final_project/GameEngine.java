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

import java.util.Random;

/**
 * This class represents the GameEngine for the dungeon game that runs the game and interacts with the {@link UI}
 *   as well as all the objects in the game. It also keeps track of the states of all the objects.
 * @author
 */
public class GameEngine{
	private Map map;
	private Ninja[] ninjas = new Ninja[6];
	private PowerUp[] powerups = new PowerUp[3];
	private Spy spy = new Spy();
	private Room[] rooms = new Room[9];
	private UI ui = new UI();
	
	private Random rand = new Random();
	private int rowSpawn, colSpawn, invincibleTurns=0;
	
	public void gameStart(){
		gameSet();
		ui.displayMenu();
		int startInput;
		do{
			startInput = ui.getIntInput();
			switch(startInput){
				case 1:
				{
					int charInput;
					boolean showDungeon = true;
					do{
						if(showDungeon)
							ui.displayDungeon(map);
						showDungeon = true;
						charInput = ui.getCharInput();
						switch(charInput){
							case 'W':
							case 'w':
							case 'S':
							case 's':
							case 'A':
							case 'a':
							case 'D':
							case 'd':
							{
								spyMove((char) charInput);
								checkForPowerUp(spy);
								if(spy.getInvincibility())
									invincibleTurns--;
								//probably have ninja attack method here
								for (int i = 0; i < 6; i++) 
									ninjas[i].move(map);
								if(invincibleTurns == 0)
									spy.disableInvincibility();
								break;
							}
							case 'Q':
							case 'q':
								spyShoot();
								break;
							case 'U':
							case 'u':
								ui.displayGameLegend();									
								break;
							case 'M':
							case 'm':
							{
								map.toggleMode();
								break;
							}
							//REMOVE BEFORE SUBMIT
							case 'R':
							case 'r':
							{
								gameSet();
								break;
							}
							case 'X':
							case 'x':
								break;
							default:
								showDungeon = ui.invalidInput();
								break;
						}
	
						
					}while(!(charInput=='x' || charInput=='X'));
					startInput = 2;
				}
				case 2:
					ui.displayEndGameMessage();
					break;
				default:
					break;
			}
		}while(startInput!=2);
	}
	private void checkForPowerUp(Spy spy)
	{
		if(map.powerUpCheck())
		{
			if(map.getPowerUp() instanceof Bullet)
				spy.addBullet();
			else if(map.getPowerUp() instanceof Invincibility)
			{
				invincibleTurns = 5;
				spy.activateInvincibility();
			}
			else//radar
			{
				for(int i=0;i<rooms.length;i++)
				{
					if(rooms[i].hasBriefcase())
						rooms[i].radarActivate();
				}
			}
			map.removePowerUp();
		}
	}
	private void spyMove(char charInput)
	{
		try {
			map.moveSpy(Character.toLowerCase(charInput));
		} catch (Exception e) {
			ui.displayInvalidMove();
		}
	}
	private void spyShoot(){
		
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
	private boolean notNearSpyStart(int x, int y){
		//TODO cleanup
		if(!((x==6 && (y==0 || y==1)) || ((x==7 ||
				x==8)&& y==2) || (x==7 && y==0) || (x==8 && y==1)))
			return true;
		else
			return false;
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
				if(notNearSpyStart(rowSpawn,colSpawn) && map.noActiveAgent(rowSpawn,colSpawn))
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
			if(map.noPowerUp(rowSpawn,colSpawn) && !map.isRoom(rowSpawn,colSpawn) && !(rowSpawn==8 && colSpawn==0))
			{
				map.set(rowSpawn, colSpawn, powerups[powerup_num]);
				powerup_num++;
			}
		}while(powerup_num<powerups.length);
	}
	private void setSpy()
	{
		map.set(8,0,spy);
	}
	private void gameSet(){
		map = new Map();
		setRooms();
		setNinjas();
		setPowerUps();
		setSpy();
	}
}
