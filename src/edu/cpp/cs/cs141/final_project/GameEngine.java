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
	private Spy spy;
	private Room[] rooms = new Room[9];
	private UI ui = new UI();
	
	private Random rand = new Random();
	private int rowSpawn, colSpawn;
	private final int tileMax = 9;
	
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
										if(!spy.getInvincibility() && isNinjaAdjacent()){
											killSpy();
											if(spy.getLives() == 0){
												endGameType = 3;
												endGame = true;
												if(Map.isDebug())
													map.toggleMode();
											}
										}
										else
										{
											for (int i = 0; i < ninjas.length; i++)
											{
												if(ninjas[i].isAlive())
												{
													if (ninjas[i].ISeeTheSpy(map, spy) == true)
														ninjas[i].moveTowardsSpy(map);
													else
														ninjas[i].move(map);
												}
											}
											checkForPowerUp(spy);									
										}
										if(spy.getInvincibleTurns() == 0)
											spy.disableInvincibility();
									}
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
			//if a ninja is in spawn area, respawn the ninja elsewhere
			for(int i=0;i<ninjas.length;i++)
			{
				if(!(notNearSpyStart(ninjas[i].getRowCoord(),ninjas[i].getColCoord())))
				{
					map.set(ninjas[i].getRowCoord(), ninjas[i].getColCoord(), new EmptyAA());
					rowSpawn = rand.nextInt(tileMax);
					colSpawn = rand.nextInt(tileMax);
					boolean spawnNinja = true;
					ninjas[i] = new Ninja();
					do{
						if(notNearSpyStart(rowSpawn,colSpawn) && map.noActiveAgent(rowSpawn,colSpawn))
						{
							map.set(rowSpawn, colSpawn, ninjas[i]);
							spawnNinja = false;
						}
						else
						{
							rowSpawn = rand.nextInt(tileMax);
							colSpawn = rand.nextInt(tileMax);
						}
					}while(spawnNinja);
				}
			}
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
		boolean moveAgainstRoom = false;
		try {
			switch(charInput)
			{
				case 's':
				case 'S':
				{
					if(map.getAtLocation(spy.getRowCoord()+1, spy.getColCoord()) instanceof Room)
					{
						moveAgainstRoom = true;
						for(int i=0;i<rooms.length;i++)
						{
							if(rooms[i].getRowCoord()==spy.getRowCoord()+1 &&
								rooms[i].getColCoord()==spy.getColCoord())
							{
								if(rooms[i].hasBriefcase())
									return true;
								else
									ui.displayEmptyRoomMessage();
								break;
							}
						}
					}
					break;
				}
				//case w,a,d: spy tries to enter a room from wrong direction
				case 'w':
				case 'W':
				{
					if(map.getAtLocation(spy.getRowCoord()-1, spy.getColCoord()) instanceof Room)
					{
						moveAgainstRoom = true;
						ui.displayInvalidRoomMove();
					}
					break;
				}
				case 'a':
				case 'A':
				{
					if(map.getAtLocation(spy.getRowCoord(), spy.getColCoord()-1) instanceof Room)
					{
						moveAgainstRoom = true;
						ui.displayInvalidRoomMove();
					}
					break;
				}
				case 'd':
				case 'D':
				{
					if(map.getAtLocation(spy.getRowCoord(), spy.getColCoord()+1) instanceof Room)
					{
						moveAgainstRoom = true;
						ui.displayInvalidRoomMove();
					}
					break;
				}
			}
			if(!moveAgainstRoom)
			{
				spy.toggleMove();
				map.moveSpy(Character.toLowerCase(charInput));
				return false;
			}
		} catch (Exception e) {
			ui.displayInvalidMove();
		}
		return false;
	}
	private void spyShoot(){
	    if (spy.getBullet() >= 1)
	    {
	    	spy.useBullet();
	    	
			boolean hit = false;
			int i = spy.getRowCoord();
			int j = spy.getColCoord();
			boolean isValidLocation = map.checkValidLocation(i, j);
			if (isValidLocation == true){
				switch(ui.displayShootMenu())
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
				for(int k=0;k<ninjas.length;k++)
				{
					if(map.getAtLocation(i,j) == ninjas[k])
						ninjas[k].kill();
				}
				
				map.set(i, j, new EmptyAA());
				ui.displayNinjaDeathMessage();
			}
	    }
	    else
	    	ui.displayNoBulletMessage();
	}
	private void spyLook(){
		char direction = ui.displayLookMenu();
		if(direction == 'W' || direction == 'w' || direction == 'A' || direction == 'a' ||
			direction == 'S' || direction == 's' || direction == 'D' || direction == 'd')
			spy.setLook(direction);
		else
			new Exception("Invalid directional input").printStackTrace();
		
		switch(spy.getLook())
		{
			case 'W':
			case 'w':
			{
				for(int i=spy.getRowCoord();i>0;i--)
				{
					if(map.getAtLocation(spy.getRowCoord()-i,spy.getColCoord()) instanceof Ninja)
					{
						ui.pathAlertMessage('w');
						return;
					}
				}
				ui.pathClearMessage('w');
				return;
			}
			case 'A':
			case 'a':
			{
				for(int i=spy.getColCoord();i>0;i++)
				{
					if(map.getAtLocation(spy.getRowCoord(),spy.getColCoord()-i) instanceof Ninja)
					{
						ui.pathAlertMessage('a');
						return;
					}
				}
				ui.pathClearMessage('a');
				return;
			}
			case 'S':
			case 's':
			{
				for(int i=0;i<tileMax-spy.getRowCoord();i++)
				{
					if(map.getAtLocation(spy.getRowCoord()+i,spy.getColCoord()) instanceof Ninja)
					{
						ui.pathAlertMessage('s');
						return;
					}
				}
				ui.pathClearMessage('s');
				return;
			}
			case 'D':
			case 'd':
			{
				for(int i=0;i<tileMax-spy.getColCoord();i++)
				{
					if(map.getAtLocation(spy.getRowCoord(),spy.getColCoord()+i) instanceof Ninja)
					{
						ui.pathAlertMessage('d');
						return;
					}
				}
				ui.pathClearMessage('d');
				return;
			}
			default:
				new Exception("Invalid directional input").printStackTrace();
		}
	}
	private void setRooms()
	{
		for(int i=0;i<rooms.length;i++)
			rooms[i] = new Room();
		rooms[rand.nextInt(rooms.length)].setBriefcase();
		  
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
		int spyStartRange = 3;
		for(int row=tileMax-spyStartRange;row<tileMax;row++)
		{
			for(int col=0;col<spyStartRange;col++)
			{
				if(x==row && y==col)
					return false;
			}
		}
		return true;
	}
	private void setNinjas()
	{
		rowSpawn = rand.nextInt(tileMax);
		colSpawn = rand.nextInt(tileMax);
		boolean spawnNinja;
		for(int i=0;i<ninjas.length;i++)
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
					rowSpawn = rand.nextInt(tileMax);
					colSpawn = rand.nextInt(tileMax);
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
			rowSpawn = rand.nextInt(tileMax);
			colSpawn = rand.nextInt(tileMax);
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