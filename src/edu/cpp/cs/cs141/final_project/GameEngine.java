/**
 * CS 141: Intro to Programming and Problem Solving
 * Professor: Edwin Rodrú„uez
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
	
	/**
	 * These fields represent all the creation of objects that the game will consist of.
	 */
	private Map map;
	private Ninja[] ninjas = new Ninja[6];
	private PowerUp[] powerups = new PowerUp[3];
	private Spy spy;
	private Room[] rooms = new Room[9];
	private UI ui = new UI();
	
	/**
	 * This object represents an object that is used to generate random numbers.
	 */
	private Random rand = new Random();
	/**
	 * These fields represent row and column locations on the {@link Map} that are used
	 * in multiple functions in this class.
	 */
	private int rowSpawn, colSpawn;
	/**
	 * This field represents the max row or column {@link Tile}'s on {@link Map}
	 */
	private final int tileMax = 9;
	
	/**
	 * This function starts the game and contains the game loop, continuously taking input.
	 * This includes displaying the program's start menu and showing the setting the game
	 * objects at the start of the game.
	 * The player has options of moving, looking, shooting, showing the dungeon legend,
	 * saving the game, and exiting the game.
	 */
	public void gameStart(){
		int startInput, endGameType = 0;
		boolean newGame = true;
		boolean exitProgram = false;
		do{
			ui.displayMenu();
			startInput = ui.getStartIntInput();
			switch(startInput){
				case 1:
				{
					ui.displayEnterDungeonMessage();
					if (newGame) {
						gameSet();
					}
					int charInput;
					boolean showDungeon = true, endGame = false;
					do{
						if(showDungeon)
							ui.displayDungeon(map,spy);
						showDungeon = true;
						charInput = ui.getCharInput();
						switch(Character.toLowerCase(charInput)) {
							case 'w':
							case 's':
							case 'a':
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
										checkForPowerUp();
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
										}
										if(spy.getInvincibleTurns() == 0)
											spy.disableInvincibility();
									}
								}
								break;
							}
							case 'e':
								spyLook();
								break;
							case 'q':
								spyShoot();
								break;
							case 'u':
								ui.displayGameLegend();									
								break;
							case 'm':
							{
								map.toggleMode();
								break;
							}
							case 'x':
							{
								endGameType = 1;
								endGame = true;
								if(Map.isDebug())
									map.toggleMode();
								break;
							}
							case 'v':
							{
								saveData();
								break;
							}
							default:
								showDungeon = false;
								ui.invalidInput();
								break;
						}
						rand.nextBoolean();
					}while(!endGame);
					newGame= true;
					if(endGameType>0 && endGameType<4)
						ui.displayEndGameMessage(endGameType);
					break;
				}
				case 2:
					boolean loading = true;
					while (loading) {
						try {
							if (!loadData()) break;
							ui.displayFileLoad();
							loading = false;
							newGame = false;
						} catch (Exception e) {
							ui.displayFileError();
						}
					}
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
	 * This occurs if it is the {@link Ninja} turn to move and the {@link Spy} is next to the {@link Ninja}.
	 * Removes one life from the {@link Spy} and moves it back to the starting position.
	 * The {@link Ninja}'s do not move when the {@link Spy} is killed.
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
			//delete the old spy (replace with EmptyAA)
			map.set(tempx, tempy, new EmptyAA());
			//reset spy's position
			setSpy();
		}
	}

	/**
	 * Checks if there is a {@link Ninja} next to the {@link Spy}.
	 * @return {@code true} if a {@link Ninja} is adjacent to the {@link Spy}
	 * (one square up, down, left, or right); {@code false} otherwise
	 */
	private boolean isNinjaAdjacent() {
		return map.isNinja(spy.getRowCoord() + 1, spy.getColCoord()) || 
			   map.isNinja(spy.getRowCoord() - 1, spy.getColCoord()) || 
			   map.isNinja(spy.getRowCoord(), spy.getColCoord() + 1) || 
			   map.isNinja(spy.getRowCoord(), spy.getColCoord() - 1);
	}
	
	/**
	 * Populates the current game data variables with information
	 * loaded from a file provided by the user. If the user specifies,
	 * the loading is cancelled and is noted by a {@code false} return.
	 * 
	 * @return {@code true} if data was loaded. {@code false} if cancelled.
	 * @throws Exception problem reading or de-serializing data file
	 */
	private boolean loadData() throws Exception  {
		String[] fileList = IO.listFiles();
		String filename = ui.getFilename(fileList);
		
		if (filename.equalsIgnoreCase("C")) return false;
		
		//If the filename is a digit, set {filename} to the corresponding String in fileList. (May throw Exception)
		if (filename.matches("\\d*")) filename = fileList[Integer.parseInt(filename)-1];
		
		if (filename.isEmpty()) filename = "save.dat";
		
		SaveData save = (SaveData) IO.load(filename);
		map = save.getMap();
		ninjas = save.getNinjas();
		spy = save.getSpy();
		rooms = save.getRooms();
		return true;
	}
	
	/**
	 * Uses the {@link IO#save(Object, String)} method to write the current game state to
	 * a file provided by the user.
	 */
	private void saveData() {
		String filename = ui.getFilename(IO.listFiles());
		if (filename.isEmpty()) filename = "save.dat";
		
		SaveData save = new SaveData(map);
		IO.save(save, filename);
	}
	
	/**
	 * This function checks for {@link PowerUp} in the same {@link Tile} that the spy is on.
	 * If there is a {@link PowerUp}, applies {@link PowerUp} and removes {@link PowerUp} from tile.
	 * @param spy is the spy object created for the game
	 */
	private void checkForPowerUp()
	{
		if(map.powerUpCheck(spy))
		{
			if(map.getPowerUp(spy) instanceof Bullet)
				spy.addBullet();
			else if(map.getPowerUp(spy) instanceof Invincibility)
				spy.activateInvincibility();
			else//radar
				showBriefcase();
			map.removePowerUp(spy);
		}
	}
	/**
	 * This function causes the room with the briefcase to reveal itself on the dungeon display.
	 */
	private void showBriefcase()
	{
		for(int i=0;i<rooms.length;i++)
		{
			if(rooms[i].hasBriefcase())
				rooms[i].radarActivate();
		}
	}
	/**
	 * This function moves the {@link Spy} around the dungeon to the next {@link Tile}
	 * @param charInput is the input that determines the direction the {@link Spy} moves
	 * @return true if moving down into a {@link Room}, otherwise returns false
	 */
	private boolean spyMove(char charInput)
	{
		boolean moveAgainstRoom = false;
		try {
			switch(Character.toLowerCase(charInput))
			{
				case 's':
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
				{
					if(map.getAtLocation(spy.getRowCoord()-1, spy.getColCoord()) instanceof Room)
					{
						moveAgainstRoom = true;
						ui.displayInvalidRoomMove();
					}
					break;
				}
				case 'a':
				{
					if(map.getAtLocation(spy.getRowCoord(), spy.getColCoord()-1) instanceof Room)
					{
						moveAgainstRoom = true;
						ui.displayInvalidRoomMove();
					}
					break;
				}
				case 'd':
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
				map.moveSpy(spy, Character.toLowerCase(charInput));
				return false;
			}
		} catch (Exception e) {
			ui.displayInvalidMove();
		}
		return false;
	}
	
	/**
	 * This function shoots a {@link Bullet} for the {@link Spy}
	 * If there is a {@link Ninja} in the path of the {@link Bullet}, the {@link Ninja} dies.
	 * If there is a {@link Room} in the path of the {@link Bullet} or if there is no {@link Ninja}
	 * in the {@link Spy}'s path, the {@link Spy} shoots at nothing, but still uses a {@link Bullet}.
	 */
	private void spyShoot(){
	    if (spy.getBullet() >= 1)
	    {	    	
			boolean hit = false, shotTaken;
			int i = spy.getRowCoord();
			int j = spy.getColCoord();
			do{
				switch(Character.toLowerCase(ui.displayShootMenu()))
				{
					case 'w':
					{
						while(!hit && i>0){
							i--;
							if(map.isRoom(i, j))
								break;
							if(map.isNinja(i, j))
								hit = true;
						}
						shotTaken = true;
						break;
					}
					case 's':
					{
						while(!hit && i<tileMax-1){
							i++;
							if(map.isRoom(i, j))
								break;
							if(map.isNinja(i, j))
								hit = true;
						}
						shotTaken = true;
						break;
					}
					case 'a':
					{
						while(!hit && j>0){
							j--;
							if(map.isRoom(i, j))
								j=0;
							if(map.isNinja(i, j))
								hit = true;
						}
						shotTaken = true;
						break;
					}
					case 'd':
					{
						while(!hit && j<tileMax-1){
							j++;
							if(map.isRoom(i, j))
								break;
							if(map.isNinja(i, j))
								hit = true;
						}
						shotTaken = true;
						break;
					}
					default:
					{
						ui.invalidInput();
						ui.displayDungeon(map, spy);
						shotTaken = false;
						break;
					}
				}
			}while(!shotTaken);
			
			if(shotTaken)
				spy.useBullet();
			
			if(hit)
			{
				for(int k=0;k<ninjas.length;k++)
				{
					if(map.getAtLocation(i,j) == ninjas[k])
						ninjas[k].kill();
				}
			
				map.set(i, j, new EmptyAA());
				ui.displayNinjaDeathMessage();
			}
			else
				ui.displayShotAir();
	    }
	    else
	    	ui.displayNoBulletMessage();
	}

	
	/**
	 * This function lets the {@link Spy} look in a direction.
	 * Gives a clear signal if no {@link Ninja} between {@link Spy} and {@link Room}/edge of dungeon.
	 * Otherwise, gives a {@link Ninja} is certain direction signal.
	 */
	private void spyLook(){
		char direction;
		boolean correctInput;
		do{
			direction = ui.displayLookMenu();
			if(direction == 'W' || direction == 'w' || direction == 'A' || direction == 'a' ||
					direction == 'S' || direction == 's' || direction == 'D' || direction == 'd')
			{
				spy.setLook(direction);
				correctInput = true;
			}
			else
			{
				ui.invalidInput();
				ui.displayDungeon(map, spy);
				correctInput = false;
			}
		}while(!correctInput);
		
		switch(Character.toLowerCase(spy.getLook()))
		{
			case 'w':
			{
				for(int i=spy.getRowCoord();i>0;i--)
				{
					if(map.getAtLocation(spy.getRowCoord()-i,spy.getColCoord()) instanceof Room)
					{
						ui.pathClearMessage('w');
						return;
					}
					if(map.getAtLocation(spy.getRowCoord()-i,spy.getColCoord()) instanceof Ninja)
					{
						ui.pathAlertMessage('w');
						return;
					}
				}
				ui.pathClearMessage('w');
				return;
			}
			case 'a':
			{
				for(int i=spy.getColCoord();i>0;i++)
				{
					if(map.getAtLocation(spy.getRowCoord(),spy.getColCoord()-i) instanceof Room)
					{
						ui.pathClearMessage('a');
						return;
					}
					if(map.getAtLocation(spy.getRowCoord(),spy.getColCoord()-i) instanceof Ninja)
					{
						ui.pathAlertMessage('a');
						return;
					}
				}
				ui.pathClearMessage('a');
				return;
			}
			case 's':
			{
				for(int i=0;i<tileMax-spy.getRowCoord();i++)
				{
					if(map.getAtLocation(spy.getRowCoord()+i,spy.getColCoord()) instanceof Room)
					{
						ui.pathClearMessage('s');
						return;
					}
					if(map.getAtLocation(spy.getRowCoord()+i,spy.getColCoord()) instanceof Ninja)
					{
						ui.pathAlertMessage('s');
						return;
					}
				}
				ui.pathClearMessage('s');
				return;
			}
			case 'd':
			{
				for(int i=0;i<tileMax-spy.getColCoord();i++)
				{
					if(map.getAtLocation(spy.getRowCoord(),spy.getColCoord()+i) instanceof Room)
					{
						ui.pathClearMessage('d');
						return;
					}
					if(map.getAtLocation(spy.getRowCoord(),spy.getColCoord()+i) instanceof Ninja)
					{
						ui.pathAlertMessage('d');
						return;
					}
				}
				ui.pathClearMessage('d');
				return;
			}
		}
	}
	/**
	 * This function sets {@link Room}'s to predetermined {@link Tile}'s.
	 * Also sets a briefcase into a random {@link Room}
	 */
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
	/**
	 * This function checks if a location is not near {@link Spy}'s location.
	 * @param x is the row location
	 * @param y is the column location
	 * @return true is not within bottom left 3x3 corner where {@link Spy} spawns
	 */
	private boolean notNearSpyStart(int x, int y){
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
	/**
	 * This function sets 6 {@link Ninja}'s randomly on {@link Map} {@link Tile}'s except within
	 * bottom left 3x3 corner.
	 */
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
	/**
	 * This function sets 3 {@link PowerUp}'s randomly on {@link Map} {@link Tile}'s except within
	 * bottom left 3x3 corner.
	 */
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
	/**
	 * This function sets {@link Spy} to bottom left {@link Tile} on {@link Map}
	 */
	private void setSpy()
	{
		map.set(8,0,spy);
	}
	/**
	 * This function instantiates a {@link Map} and calls functions which set {@link Room}'s
	 * {@link Ninja}'s, {@link PowerUp}'s, and the {@link Spy}
	 */
	private void gameSet(){
		map = new Map();
		setRooms();
		setNinjas();
		setPowerUps();
		spy = new Spy();
		setSpy();
	}
}
