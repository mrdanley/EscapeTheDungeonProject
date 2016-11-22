/**
 * CS 141: Intro to Programming and Problem Solving
 * Professor: Edwin Rodr�guez
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
	private Spy spy;
	private Room[] rooms = new Room[9];
	private UI ui = new UI();
	
	private Random rand = new Random();
	private int rowSpawn, colSpawn;
	
	public void gameStart(){
		int startInput, endGameType = 0;
		boolean exitProgram = false;
		do{
			gameSet();
			ui.displayMenu();
			startInput = ui.getIntInput();
			switch(startInput){
				case 1:
				{
					int charInput;
					boolean showDungeon = true, endGame = false;
					do{
						if(showDungeon)
							ui.displayDungeon(map,spy);
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
								spy.setMove(false);
								if(spyMove((char) charInput))
								{
									showBriefcase();
									ui.displayDungeon(map,spy);
									endGameType = 2;
									endGame = true;
									if(Map.isDebug())
										map.toggleMode();
								}
								else
								{
									if(spy.getMove())
									{
										if(spy.getInvincibility())
											spy.invincibleLoss();
										for (int i = 0; i < ninjas.length; i++) 
											ninjas[i].move(map);
									}
									checkForPowerUp(spy);
									if(!spy.getInvincibility() && isNinjaAdjacent()){
										killSpy();
										if(spy.getLives() == 0){
											endGameType = 3;
											endGame = true;
											if(Map.isDebug())
												map.toggleMode();
										}
									}
									if(spy.getInvincibleTurns() == 0)
										spy.disableInvincibility();
								}
								break;
							}
							case 'E':
							case 'e':
								spyLook();
								break;
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
							{
								endGameType = 1;
								endGame = true;
								if(Map.isDebug())
									map.toggleMode();
								break;
							}
							case 'V':
							case 'v':
								break;
							default:
								showDungeon = ui.invalidInput();
								break;
						}
					}while(!endGame);
					if(endGameType>0 && endGameType<4)
						ui.displayEndGameMessage(endGameType);
					break;
				}
				case 2://load game
					break;
				case 3:
				{
					exitProgram = true;
					break;
				}
				default:
					break;
			}
		}while(!exitProgram);
		ui.displayExitProgramMessage();
	}
	
	/**
	 * Removes one life from the spy and moves it back to the starting position.
	 */
	private void killSpy() {

		int lives = spy.loseLife();
		ui.displayDungeon(map,spy);
		ui.displaySpyDieMessage();
		if(lives > 0){
			//store spy's location in temp variables
			int tempx = spy.getRowCoord();
			int tempy = spy.getColCoord();
			//reset spy's position
			setSpy();
			//delete the old spy (replace with EmptyAA)
			map.set(tempx, tempy, new EmptyAA());
		}
	}

	/**
	 * Checks if there is a ninja next to the spy.
	 * @return {@code true} if a ninja is adjacent to the spy (one square up, down, left, or right); {@code false} otherwise
	 */
	private boolean isNinjaAdjacent() {
		return map.isNinja(spy.getRowCoord() + 1, spy.getColCoord()) || 
			   map.isNinja(spy.getRowCoord() - 1, spy.getColCoord()) || 
			   map.isNinja(spy.getRowCoord(), spy.getColCoord() + 1) || 
			   map.isNinja(spy.getRowCoord(), spy.getColCoord() - 1);
	}
	
	private void checkForPowerUp(Spy spy)
	{
		if(map.powerUpCheck())
		{
			if(map.getPowerUp() instanceof Bullet)
				spy.addBullet();
			else if(map.getPowerUp() instanceof Invincibility)
				spy.activateInvincibility();
			else//radar
				showBriefcase();
			map.removePowerUp();
		}
	}
	private void showBriefcase()
	{
		for(int i=0;i<rooms.length;i++)
		{
			if(rooms[i].hasBriefcase())
				rooms[i].radarActivate();
		}
	}
	private boolean spyMove(char charInput)
	{
		try {
			for(int i=0;i<rooms.length;i++)
			{
				if(rooms[i].getRowCoord()==spy.getRowCoord()+1 &&
					rooms[i].getColCoord()==spy.getColCoord() && (charInput=='s' || charInput=='S'))
				{
					if(rooms[i].hasBriefcase())
						return true;
					else
					{
						spy.toggleMove();
						ui.displayEmptyRoomMessage();
					}
				}
			}
			map.moveSpy(Character.toLowerCase(charInput));
			spy.toggleMove();
			return false;
		} catch (Exception e) {
			ui.displayInvalidMove();
		}
		return false;
	}
	private void spyShoot(){
	    if (spy.getBullet() >= 1){
	    	spy.useBullet();
	    	
	    	System.out.println("Choose the direction you want to shoot\n");
			System.out.println("[W]Up [S]Down [A]Left [D]Right\n");
			int direction = ui.getCharInput();
			boolean hit = false;
			int i = spy.getRowCoord();
			int j = spy.getColCoord();
			boolean isValidLocation = map.checkValidLocation(i, j);
			if (isValidLocation == true){
				switch(direction)
				{
					case 'W':
					case 'w':
						while(!hit){
							i -= 1;
							if(map.isNinja(i, j)){
								hit = true;
							}
						}
						break;
					case 'S':
					case 's':
						while(!hit){
							i += 1;
							if(map.isNinja(i, j)){
								hit = true;
							}
						}
						break;
					case 'A':
					case 'a':
						while(!hit){
							j -= 1;
							if(map.isNinja(i, j)){
								hit = true;
							}
						}
						break;
					case 'D':
					case 'd':
						while(!hit){
							j += 1;
							if(map.isNinja(i, j)){
								hit = true;
							}
						}
						break;
						
				}
				map.set(i, j, new EmptyAA());
				System.out.println("You killed one ninja");
			}
	    }	
	}
	private void spyLook(){
		
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
		spy = new Spy();
		setSpy();
	}
}
